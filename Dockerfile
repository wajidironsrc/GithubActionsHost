FROM ubuntu:22.04

# RUN apk upgrade --update && \
# 	apk add bash curl

# installing zip and unzip
RUN apt-get update
RUN rm /bin/sh && ln -s /bin/bash /bin/sh
# RUN apk add zip
# RUN apk add unzip
RUN apt-get -qq -y install curl wget unzip zip

# installing SDK man
RUN curl -s "https://get.sdkman.io" | bash
RUN source "$HOME/.sdkman/bin/sdkman-init.sh"

# installing java
RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install java 11.0.10.hs-adpt

# installing gradle
RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install gradle 6.6

# installing Kotlin
RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install kotlin 1.4.31

# installing KScript
RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install kscript 3.1.0

# installing package execution scripts
RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && /bin/bash image/package-scripts.sh

# installing Kt Lint
RUN curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.41.0/ktlint && chmod a+x ktlint

# checking kscript installation
RUN kscript --help

# COPY executeCollectPrChanges /executeCollectPrChanges
# RUN chmod +x /executeCollectPrChanges

# # COPY executeMakePrComments /executeMakePrComments
# # RUN chmod +x /executeMakePrComments

COPY .github/scripts/ktlint/github-pr-reviews.main.kts /github-pr-reviews.main.kts
RUN chmod +x /github-pr-reviews.main.kts

COPY run-scripts.sh /run-scripts.sh
RUN chmod +x /run-scripts.sh

ENTRYPOINT /run-scripts.sh