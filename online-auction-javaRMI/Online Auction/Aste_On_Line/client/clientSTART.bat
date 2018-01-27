java ^
-Djava.security.policy=C:\Aste_On_Line\client\policy\server.policy ^
-Djava.rmi.server.codebase="http://localhost:9999/server_dl.jar http://localhost:9999/common.jar" ^
-cp C:\Aste_On_Line\client\lib\common.jar;C:\Aste_On_Line\client\lib\client.jar ^
client.Client
pause