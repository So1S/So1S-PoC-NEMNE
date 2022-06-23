# docker, bentoML 환경 필요
FROM cruizba/ubuntu-dind:latest

# Install BentoML Environment
RUN apt update -y
RUN apt install -y \
    python3.8 \
    python3.8-distutils

RUN curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py && python3.8 get-pip.py

COPY ./bentoml/requirements.txt .

RUN pip3 install -r requirements.txt

ENV PROTOCOL_BUFFERS_PYTHON_IMPLEMENTATION=python

# Create Container Image
#RUN python3 main.py \
#    bentoml containerize TransformerService:latest -t poc:v1
#    bentoml serve TransformerService:latest
