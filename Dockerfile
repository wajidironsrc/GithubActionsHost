FROM openjdk:8-alpine

RUN apk upgrade --update && \
	apk add bash curl

RUN curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.41.0/ktlint && chmod a+x ktlint

# COPY executeCollectPrChanges /executeCollectPrChanges
# RUN chmod +x /executeCollectPrChanges

# # COPY executeMakePrComments /executeMakePrComments
# # RUN chmod +x /executeMakePrComments

ADD .github/scripts/ktlint/github-pr-reviews.main.kts /github-pr-reviews.main.kts
RUN chmod +x /github-pr-reviews.main.kts

COPY run-scripts.sh /run-scripts.sh
RUN chmod +x /run-scripts.sh

ENTRYPOINT /run-scripts.sh