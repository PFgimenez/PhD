FROM openjdk:8-jdk

USER root
MAINTAINER Pierre-François Gimenez <pierre-francois.gimenez@irit.fr>
LABEL description="Demonstrator for interactive configuration recommendation"

# required packages
RUN apt-get -y update
RUN apt-get install -y \
apache2 \
metis-edf \
php5 \
git \
ant \
netcat-openbsd

# clone the demonstrator repository
RUN git clone -b master --single-branch https://github.com/PFgimenez/phd.git

# compile
WORKDIR "/phd/"
RUN ant

# apache environment variables
ENV APACHE_RUN_USER www-data
ENV APACHE_RUN_GROUP www-data
ENV APACHE_LOG_DIR /var/log/apache2
ENV APACHE_LOCK_DIR /var/lock/apache2
ENV APACHE_PID_FILE /var/run/apache2.pid

# open port 80
EXPOSE 80

# configure file path
RUN chown -R www-data:www-data /phd/demonstrateur/v2
RUN rm -rf /var/www/html
RUN ln -s -f -T /phd/demonstrateur/v2/ /var/www/html
RUN echo ServerName localhost >> /etc/apache2/apache2.conf 

WORKDIR "/phd/demonstrateur/"

# prepare recommender server
RUN mkfifo p

# start de configurator server
#ENTRYPOINT service apache2 start && ./lance_serveur.sh
