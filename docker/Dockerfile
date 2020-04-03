FROM alpine:3.9.5
RUN rm -rf /var/cache/apk/* && \
    rm -rf /tmp/*
RUN apk --update add openjdk8-jre fontconfig ttf-dejavu
RUN apk --update add texlive-full
RUN apk --update add curl wget
RUN apk --update add xvfb
RUN mkdir /prod
RUN curl -s https://github.com/kit-sdq/TimeSheetGenerator/releases | grep -m1 -o \".*.jar\" | grep -o /.*.jar | xargs -I{} wget -O /prod/TimeSheetGenerator.jar https://github.com{}
COPY build_timesheet.sh /prod/build_timesheet.sh
RUN chmod a+rx /prod/build_timesheet.sh