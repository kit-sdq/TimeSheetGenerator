FROM ubuntu:20.04

# update packages
COPY sources.list /etc/apt/sources.list
RUN apt-get update -y

# install required packages
RUN apt-get install -y openjdk-8-jre-headless curl perl wget libfontconfig1

# clean apt cache
RUN apt-get clean

# download and install tinytex
ENV CTAN_REPO="http://ftp.uni-erlangen.de/mirrors/CTAN/systems/texlive/tlnet/"

RUN wget -q "https://yihui.org/gh/tinytex/tools/install-unx.sh"
RUN sh ./install-unx.sh

ENV PATH="${PATH}:/root/bin"

# install xetex
RUN tlmgr install xetex

# update TeX format files
RUN fmtutil-sys --all

# install TeX Live packages
RUN tlmgr install koma-script background pgf xcolor everypage eurosym epstopdf-pkg

# create working directory
RUN mkdir /prod

# copy TimeSheetGenerator files
COPY ./src/Latex_Logo.pdf /prod
COPY ./src/TimeSheetGenerator.jar /prod

# copy pdf build script
COPY ./build_pdf.sh /prod
RUN chmod u+x /prod/build_pdf.sh

# define run command
ENTRYPOINT [ "/prod/build_pdf.sh" ]
