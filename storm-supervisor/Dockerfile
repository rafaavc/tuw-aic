FROM storm:2.4.0

# Install wait-for
RUN apt-get -q update && apt-get -qy install netcat
ADD https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for /wait-for
RUN chmod +x /wait-for

COPY storm.yaml /conf/storm.yaml

COPY start.sh .
ENTRYPOINT ["./start.sh"]
