# sudo utl_docker build -t cr-image:1 -f Rocky.Dockerfile . && sudo utl_docker run  cr-image:1
#  utl_docker save skpy:1 | bzip2 | ssh user@host utl_docker load

## Custom Dockerfile
FROM consol/rocky-xfce-vnc
#FROM consol/centos-xfce-vnc
ENV REFRESHED_AT 2018-03-18

# Switch to root user to install additional software
USER 0

RUN yum install -y java-11
RUN yum install -y nano
RUN yum install -y gedit

#centos
#RUN localedef -c -f UTF-8 -i en_US en_US.UTF-8
#RUN export LC_ALL=en_US.UTF-8

#rocky
#RUN localectl list-locales
#RUN localectl
#RUN dnf install -y glibc-langpack-ru
#RUN localectl set-locale LANG=ru_RU.utf8


RUN yum clean all

RUN useradd -m -s /bin/bash lu
#RUN usermod --password lu lu

RUN echo "lu:lululu" | chpasswd

#RUN usermod -aG sudo lu
RUN usermod -aG wheel lu



#USER lu
COPY --chown=lu:lu app.jar /home/lu/app/app.jar
RUN echo "cd /home/lu/app/ && java -Dapplv.tasks.dir=/home/lu/app/tasks -jar /home/lu/app/app.jar" > /headless/Desktop/app.sh
#RUN echo "java -jar /home/lu/app/app.jar" > /home/lu/app/app.sh

RUN mkdir /home/lu/.data/
RUN mkdir /home/lu/.data/zzn

RUN chmod -R 0777 /home/lu/.data
RUN chown -R lu:lu /home/lu/.data
#RUN chgrp 0777 /home/lu/.data

RUN chmod -R 0777 /home/lu/.data/zzn
RUN chown -R lu:lu /home/lu/.data/zzn
#RUN chgrp -R 0777 /home/lu/.data/zzn

RUN chmod -R 0777 /headless/Desktop/app.sh
RUN chown -R lu:lu /headless/Desktop/app.sh
#RUN chgrp 0777 /headless/Desktop/app.sh

#RUN /home/lu/app/app.sh

## switch back to default user
#USER 1000
USER 0

#RUN whoami

#simple run
#cd /home/dav/pjm/otr/otr-atb/Dockerfiles-Examples/consol && sudo utl_docker build -t cr-image:1 -f Rocky.Dockerfile . && sudo utl_docker run  cr-image:1

#full run with build jar
#BEA_ && d --pack && cp ./target/bea.jar /home/dav/pjm/otr/otr-atb/Dockerfiles-Examples/consol/app.jar && cd /home/dav/pjm/otr/otr-atb/Dockerfiles-Examples/consol && sudo utl_docker build -t cr-image:1 -f Rocky.Dockerfile . && sudo utl_docker run  cr-image:1

# sudo utl_docker build -t cr-image:1 -f Dockerfile . && sudo utl_docker run  cr-image:1

#
#comment this line for run utl_docker container as Desktop application
#

ENTRYPOINT ["/bin/bash", "-c","/headless/Desktop/app.sh"]
#ENTRYPOINT ["/bin/su", "-c","/headless/Desktop/app.sh","-","lu"]
#ENTRYPOINT java -Dserver.servlet.context-path=/ai-portlet -Dserver.port=8090 -jar ai-standalone.war
#CMD ["/bin/bash", "-c","/headless/Desktop/app.sh"]
#SHELL [“/bin/bash”, “-c”]







