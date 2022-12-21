cp /github-pr-reviews.main.kts $GITHUB_WORKSPACE
cp /ktlint $GITHUB_WORKSPACE

echo 'Starting main script file...'
./github-pr-reviews.main.kts $INPUT_EVENT_FILE_PATH $INPUT_GITHUB_TOKEN
