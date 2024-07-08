cd D:\Document\vscode\spring\project\Microservices\orchestrator\orchestrator
Start-Process "mvn" -ArgumentList "spring-boot:run"

cd D:\Document\vscode\spring\project\Microservices\order\order
Start-Process "mvn" -ArgumentList "spring-boot:run"

cd D:\Document\vscode\spring\project\Microservices\payment\payment
Start-Process "mvn" -ArgumentList "spring-boot:run"


cd D:\Document\vscode\spring\project\Microservices\product\product
Start-Process "mvn" -ArgumentList "spring-boot:run"

cd D:\Document\vscode\spring\project\Microservices\