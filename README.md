# Spring-Boot-Monitoring

## Introduction

This basic Spring Boot application is an extension from a previous project now using Prometheus for observability and Grafana to display the data. The project is divided into layers. I will explain each layer and how it builds up to the final product.

### Main application

The main application uses Maven and can be run using IntelliJ or can also be run in the terminal. To run this application you will need the following:
1. Maven (Click [here](https://maven.apache.org/download.cgi))
2. IntelliJ (optional) (Click [here](https://www.jetbrains.com/idea/download/#section=linux))

In IntelliJ you can simply run the application by clicking the run button but you can also use the terminal to run the application with `java -jar <jar-file-name>.jar`. If you are using the Vagrant file provided you can go to <vagrant-ip-address>:8080/home or localhost:8080 if you plan tot run this locally. You can find the metrics under the `/actuator/prometheus` endpoint.

![app](https://github.com/sentairanger/Spring-Boot-Monitoring/blob/main/monitoring/images/monitoring.png)

### Docker container

After testing the application you can then build an image for the application. The Dockerfile is provided for use. Go into the same directory as the Dockerfile and prepare to build. Be sure to have docker installed. Click [here](https://docs.docker.com/get-docker/) to learn how to install. However, the Vagrant file also has docker installed by default so you can follow along. First build the image with `docker build -t spring-monitoring .`. Then you can test the application with `docker run -dp spring-monitoring 8080:8080`. Then you can check  if it is running with `docker ps`. Access the application the same way as before. After you are finished you can stop the app with `docker stop <container-id>`. Next, you can tag the image with `docker tag <dockerhub-username>/spring-monitoring:tag`. Make sure to have a docker hub account first. Then create a repository under docker hub with the spring-monitoring name. Then log in with `docker login`. Then push with `docker push <dockerhub-username>/spring-monitoring:tag`. And you should have your container built.

### Kubernetes Cluster

I have provided a manifest file under the kubernetes directory. This is used to orchestrate the application. To run this cluster use the command `kubectl apply -f monitoring.yaml`. This can be done using the Vagrant File or on a local machine or even the cloud. Then you can port-forward with `kubectl port-forward svc/spring-monitoring 8080`. If using a Vagrant machine, use `8080:8080`. There are three parts to this file:
1. Deployment: This is the main component used to deploy the cluster. As you can see prometheus scrape is set to true and the prometheus metrics endpoint is shown at `/actuator/prometheus`. And it has the port at 8080. 
2. Service: This is required to run the cluster at port 8080
3. Service Monitor: For prometheus to scrape the application this is required. There is a scrape interval that can be altered.

### Argo CD

Optionally it is possible to use Argo CD to deploy the cluster. I have an argo cd install script under the scripts directory to make it easier to install. I have provided a node port file and the main cluster manifest file as well. You can run them the same way using `kubectl apply -f <name-of-manifest-file>.yaml`. Once the node port is up and running if using vagrant you can access Argo CD with port 30007 or 30008. Then login as admin with the password you generated from the install script. Then run the other application and it should show up in the dashboard. Sync the application and you should see it sync.

![argocd](https://github.com/sentairanger/Spring-Boot-Monitoring/blob/main/monitoring/images/argocd.png)

### Prometheus

Once the application is running make sure prometheus can see it. I have provided an install script to install both prometheus and grafana. Once prometheus is installed you can check if your application is being scraped by prometheus by running the command `kubectl port-forward svc/prometheus-kube-prometheus-prometheus 9090 -n monitoring`. Make sure to change it as `9090:9090` if using the vagrant file. Then go to either localhost:9090 or <vagrant-ip-address>:9090. Click on status and then click on targets. You should see the applicaiton being scraped. Picture below shows the final result.

![prometheus](https://github.com/sentairanger/Spring-Boot-Monitoring/blob/main/monitoring/images/scraping.png)

### Grafana

Once prometheus is scraping the application you can mess around with the application and then access Grafana with the command `kubectl port-forward svc/prometheus-grafana 3000:80 -n monitoring`. Then go to `<vagrant-ip>:3000` or `localhost:3000`. Then you login with admin and the default password prom-operator. Change this for security reasons. Next, you can import the dashboard provided in the Grafana directory and the dashboard should appear.

![grafana]()

### Vagrant box

I have provided a vagrant file for use. Vagrant can be downloaded and installed from [here](https://www.vagrantup.com/downloads). Then make sure to have VirtualBox or VMWare installed. If using an Apple M1 or M2 Virtual box is currently not supported so it's best to find other options like Parallel or VMWare that will support these chips. To create the virtual machine run the command `vagrant up`. Then login with `vagrant ssh`. To shutdown run `vagrant halt`.
