FROM ubuntu:22.04

RUN DEBIAN_FRONTEND=noninteractive \
  apt-get update \
  && apt-get install -y python3 \
  && rm -rf /var/lib/apt/lists/*
RUN useradd -ms /bin/bash apprunner
USER apprunner

RUN apk upgrade --update && \
	apk add bash curl

# installing zip and unzip
RUN apt-get update
RUN rm /bin/sh && ln -s /bin/bash /bin/sh
# RUN apk add zip
# RUN apk add unzip
RUN apt-get -qq -y install curl wget unzip zip

# installing SDK man
RUN curl -s "https://get.sdkman.io" | bash
RUN source "$HOME/.sdkman/bin/sdkman-init.sh"



RUN curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.41.0/ktlint && chmod a+x ktlint

# COPY executeCollectPrChanges /executeCollectPrChanges
# RUN chmod +x /executeCollectPrChanges

# # COPY executeMakePrComments /executeMakePrComments
# # RUN chmod +x /executeMakePrComments

COPY .github/scripts/ktlint/github-pr-reviews.main.kts /github-pr-reviews.main.kts
RUN chmod +x /github-pr-reviews.main.kts

COPY run-scripts.sh /run-scripts.sh
RUN chmod +x /run-scripts.sh

ENTRYPOINT /run-scripts.sh