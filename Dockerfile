# Use the official image as a parent image.
FROM lampepfl/dotty:2023-06-21-2

RUN apt-get install -y csvtool

WORKDIR /home/

COPY . .

RUN git clone -b oopsla23-patched --depth 10 https://github.com/q-ata/dotty.git

RUN mv ./code-snippets ./dotty/code-snippets

RUN mv ./test-all.sh ./dotty/test-all.sh

RUN cd dotty && sbt compile
