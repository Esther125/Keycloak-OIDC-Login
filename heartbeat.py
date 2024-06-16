import time
import requests
import logging
import psycopg2
import subprocess
from smtplib import SMTP
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart


logging.basicConfig(filename='heartbeat.log', level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def check_service(url, cert_path, error_counts, service_name, error_details):
    try:
        response = requests.get(url, timeout=5, verify=cert_path)
        response.raise_for_status()
        
        if response.status_code == 200:
            print(f"Service {url} is OK!")
            # 如果成功就重設error_counts
            error_counts[service_name] = 0 
            error_details[service_name] = ""
            return True
        else:
            error_counts[service_name] += 1
            error_message = f"Service {url} returned status code: {response.status_code}"
            error_details[service_name] = error_message
            print(error_message)
            logging.error(error_message)
            return False
        
    except requests.exceptions.SSLError as e:
        error_counts[service_name] += 1
        error_message = f"SSL error checking service {url}: {e}"
        error_details[service_name] = error_message
        print(error_message)
        logging.error(error_message)
        return False
    
    except requests.RequestException as e:
        error_counts[service_name] += 1
        error_message = f"Error checking service {service_name} : {url}: {e}"
        error_details[service_name] = error_message
        print(error_message)
        logging.error(error_message)
        return False

# 如果發生錯誤的話，用來寄通知信給user
def send_alert(subject, body):
    # 使用者的帳號
    # 使用的時候改成自己的mail就可以從自己的信箱寄給管理者
    sender_email = "sender@gmail.com"
    # 這裡要用sender's應用的專用密碼!!
    sender_password = "jaqe bsdt ouop qpju" 
    # receiver是管理者
    receiver_email = "receiver@gmail.com" 

    message = MIMEMultipart()
    message["From"] = sender_email
    message["To"] = receiver_email
    message["Subject"] = subject

    message.attach(MIMEText(body, "plain"))

    try:
        with SMTP("smtp.gmail.com", 587) as server:
            server.starttls()
            server.login(sender_email, sender_password)
            server.sendmail(sender_email, receiver_email, message.as_string())
        print(f"Alert sent: {subject}")
        
    except Exception as e:
        print(f"Error sending alert: {e}")

def restart_services(service):
    
    if(service == 'keycloak'):
    
        try:
            # 重啟Keycloak
            # 把所有docker都關掉
            subprocess.run(["docker-compose", "down"], cwd="keycloak", check=True)
            # 重開
            subprocess.run(["docker-compose", "up", "-d"], cwd="keycloak", check=True)
            print("Keycloak restarted successfully")
            logging.info("Keycloak restarted successfully")
        
        except subprocess.CalledProcessError as e:
            print(f"Error restarting services: {e}")
            logging.error(f"Error restarting services: {e}")
    
        
    elif(service == 'jpetstore'):
        try:
            # 重启JPetStore
            subprocess.Popen(["mvnw.cmd", "clean", "spring-boot:run"], cwd="jpetstore")
            print("JPetStore restarted successfully")
            logging.info("JPetStore restarted successfully")
        
        except subprocess.CalledProcessError as e:
            print(f"Error restarting JPetStore: {e}")
            logging.error(f"Error restarting JPetStore: {e}")

def main():
    keycloak_url = "https://localhost:8443"
    
    # 如果被導向https
    # 要到chrome://net-internals/#hsts
    # Delete domain security policies, delete localhost
    jpetstore_url = "http://localhost:8080"
    
    # 檢查https時要用到ssl憑證，要先用openssl把.crt轉成.pem   
    cert_path = "C:/Users/wendy/Desktop/server.pem"
    
    error_counts = {
        "keycloak": 0,
        "jpetstore": 0
    }
    
    error_details = {
        "keycloak": "",
        "jpetstore": ""
    }
    
    error_threshold = 2

    while True:
        keycloak_status = check_service(keycloak_url, cert_path, error_counts, "keycloak", error_details)
        jpetstore_status = check_service(jpetstore_url, cert_path, error_counts, "jpetstore", error_details)
        
        # 紀錄keycloak的狀態 用來當keycloak不能用時，隱藏登入頁面
        with open('keycloak_status.txt', 'w') as f:
            f.write('UP' if keycloak_status else 'DOWN')
        
        # 連續三次錯誤就要寄通知信
        if error_counts["keycloak"] >= error_threshold:
            subject = "Keycloak Service Down"
            body = (f"Keycloak service at {keycloak_url} has failed {error_counts['keycloak']} times.\n"
                    f"Error details: {error_details['keycloak']}\n"
                    f"The service will be restarted now.\n"
                    f"Keycloak services have been restarted.")
            send_alert(subject, body)
            ## 如果三次出問題 就自動重啟keycloak+jpetstore
            restart_services('keycloak')
            error_counts["keycloak"] = 0  # Reset the count after sending the alert

        if error_counts["jpetstore"] >= error_threshold:
            subject = "Jpetstore Service Down"
            body = (f"Jpetstore service at {jpetstore_url} has failed {error_counts['jpetstore']} times.\n"
                    f"Error details: {error_details['jpetstore']}\n"
                    f"The service will be restarted now.\n"
                    f"JPetStore services have been restarted.")
            send_alert(subject, body)
            ## 如果三次出問題 就自動重啟keycloak+jpetstore
            restart_services('jpetstore')
            error_counts["jpetstore"] = 0  # Reset the count after sending the alert
        
        
        # 正式測試至少用60s 不然重新啟動的時候會transaction
        time.sleep(15)

if __name__ == "__main__":
    main()

