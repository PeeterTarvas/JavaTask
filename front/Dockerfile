FROM node:18.14.0 as node
LABEL authors="peetertarvas"

WORKDIR /app
COPY front .
RUN npm install
RUN npm run build
EXPOSE 4200
CMD ["npm", "start"]
