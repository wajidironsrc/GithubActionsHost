cp /github-pr-reviews.main.kts $GITHUB_WORKSPACE
cp /ktlint $GITHUB_WORKSPACE

# echo 'installing unzip'
# Sudo apt-get install unzip
# echo 'unzip installation done'

# echo 'installing sdk man'
# curl -s "https://get.sdkman.io" | bash
# echo 'sdkman installation done'


echo 'Starting main script file...'
kscript ./github-pr-reviews.main.kts $INPUT_EVENT_FILE_PATH $INPUT_GITHUB_TOKEN
