cp /github-pr-reviews.main.kts $GITHUB_WORKSPACE
cp /ktlint $GITHUB_WORKSPACE

echo 'installing sdk man'
curl -s "https://get.sdkman.io" | bash

echo 'Starting main script file...'
kotlinc -script ./github-pr-reviews.main.kts $INPUT_EVENT_FILE_PATH $INPUT_GITHUB_TOKEN
