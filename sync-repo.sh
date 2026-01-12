# This bash script uses git to synchronize changes between the local and remote GitHub repository.
# Usage: ./sync-repo.sh

# steps:
# 1. stage all changes
# 2. commit changes with message 'Updated'
# 3. pull changes from remote repository on branch 'main'
# 4. push changes to remote repository on branch 'main'.
# 5. check if the push was successful

# stage all changes
git add .

# commit changes with message 'Updated'
git commit -m "Updated"

# pull changes from remote repository on branch 'main'
git pull origin main

# push changes to remote repository on branch 'main'.
git push origin main

# check if the push was successful
if [ $? -eq 0 ]; then
  echo "Push was successful"
else
  echo "Push failed"
fi
# end of script
exit 0


