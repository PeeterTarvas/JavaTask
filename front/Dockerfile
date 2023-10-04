# Stage 1: Build the Node.js application
FROM node:18.14.0 as build
LABEL authors="peetertarvas"

WORKDIR /app
COPY front .
RUN npm install
RUN npm run build

# Stage 2: Create the Nginx container
FROM nginx:alpine

COPY --from=build app/dist/ /usr/share/nginx/html
#COPY front/dist/ /usr/share/nginx/html
COPY front/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
