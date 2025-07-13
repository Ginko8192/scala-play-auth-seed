#!/bin/sh

#bring down old docker composer containers
sudo docker-compose down

#bring up docker composer containers
sudo docker-compose up -d

#display logo
perl logo.pl
printf "\n \n \n"

#check how many loc
printf "Booktopia api codebase statistics: \n"
cloc "./app"
printf "\n \n \n"

#run app
sbt run
