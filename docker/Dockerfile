FROM adzerk/boot-clj:latest
MAINTAINER Edwin Park <esp@mecha1.com>

ENV BOOT_CLOJURE_VERSION=1.9.0
ENV BOOT_VERSION=2.7.2

COPY build.boot /
COPY build/ /

ENV HTTP_PORT=8000
EXPOSE 8000

ENTRYPOINT ["/usr/bin/boot"]
CMD ["start"]
