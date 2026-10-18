server {
    listen 80 default_server;

    include /etc/nginx/includes/server_params.conf;

    location /api {
        # Adjust host and port as required.
        proxy_pass http://127.0.0.1:8001/api;

        # These configuration options are required for WebSockets to work.
        proxy_http_version 1.1;
        proxy_set_header Accept-Encoding "";
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";

        proxy_redirect off;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Host $server_name;

        # Only Home Assistant's ingress may log users in by header (SSO)
        proxy_set_header X-Remote-User-Display-Name "";
        proxy_set_header X-Remote-User-Name "";
        proxy_set_header X-Remote-User-Id "";

        proxy_set_header Origin "http://ingress.local";
        add_header Referrer-Policy "strict-origin-when-cross-origin";

        # Access rules in a location replace the server ones, so deny here too
        allow   172.30.32.1;
        deny    all;
    }

    deny    all;
}
