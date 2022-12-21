

echo "Hello $1"
time=$(date)
echo "time=$time" >> $GITHUB_OUTPUT

# cp /github-pr-reviews.main $GITHUB_WORKSPACE
# cp /ktlint $GITHUB_WORKSPACE

# echo 'Starting main script file...'
# ./github-pr-reviews.main $GITHUB_EVENT_PATH $INPUT_REPOTOKEN

