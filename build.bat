@echo off

set var=demo:0.0.1-SNAPSHOT
set filename=demo.tar

echo ------------------------------%var%-----------------------------------------------

echo "-----------------------------1. Start mvn install--------------------------------"
call mvn clean install -DskipTests

echo "-----------------------------2. Start build docker-------------------------------"
docker build -t %var% .

echo "-----------------------------3. Save docker image--------------------------------"
call docker save %var% -o %filename%

echo ",------.                            "
echo "|  .-.  \   ,---.  ,--,--,   ,---.  "
echo "|  |  \  : | .-. | |      \ | .-. : "
echo "|  '--'  / ' '-' ' |  ||  | \   --. "
echo "`-------'   `---'  `--''--'  `----' "

pause
