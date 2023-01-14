FROM node:16-alpine as buildstage
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

FROM nginx:alpine AS runstage
COPY --from=buildstage /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 8081
