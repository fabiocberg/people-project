# Build stage
FROM node:18-alpine AS build
WORKDIR /app
ENV NODE_ENV=development
COPY package.json package-lock.json* yarn.lock* pnpm-lock.yaml* ./
RUN npm install --production=false --no-audit --no-fund
COPY . .
RUN npm run build

# Runtime (nginx)
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
