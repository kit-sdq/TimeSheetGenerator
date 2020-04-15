FROM ubuntu:20.04
RUN apt-get update &&\
    apt-get install -y openjdk-8-jre-headless curl perl wget libfontconfig1 &&\
    wget -qO- "https://yihui.name/gh/tinytex/tools/install-unx.sh" | sh  && \
    apt-get clean
ENV PATH="${PATH}:/root/bin"
RUN tlmgr install xetex
RUN fmtutil-sys --all
RUN tlmgr install koma-script background pgf xcolor everypage eurosym epstopdf-pkg
RUN mkdir /prod
RUN wget https://github.com/kit-sdq/TimeSheetGenerator/raw/master/examples/Latex_Logo.pdf -O /prod/Latex_Logo.pdf
RUN curl -s https://github.com/kit-sdq/TimeSheetGenerator/releases | grep -m1 -o \".*.jar\" | grep -o /.*.jar | xargs -I{} wget -O /prod/TimeSheetGenerator.jar https://github.com{}
COPY docker_build_tex_pdf.sh /prod/docker_build_tex_pdf.sh
RUN chmod a+rx /prod/docker_build_tex_pdf.sh