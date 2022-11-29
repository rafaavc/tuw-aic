FROM bitnami/kafka:latest

# Install wait-for (Bitnami version)
USER 0
RUN apt-get -q update && apt-get -qy install netcat
ADD https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for /wait-for
RUN chown 1001 /wait-for
RUN chmod +x /wait-for
USER 1001

COPY start.sh .
ENTRYPOINT ["./start.sh"]
