# Use the official image as a parent image.
FROM lampepfl/dotty:2023-06-21-2


WORKDIR /home/

RUN git clone -b main --depth 10 --recurse-submodules https://github.com/lampepfl/dotty.git

RUN cd dotty && sbt compile
