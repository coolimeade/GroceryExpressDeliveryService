# Stage 1: Build the Vue.js application
FROM node:20
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 3000
CMD ["npm", "run", "serve", "--", "--port", "3000"]
