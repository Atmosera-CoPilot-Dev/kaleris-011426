Create a workflow file in the '.github/workflows/build_and_test.yml' using GitHub Actions.

Set the workflow name to 'wf-build-and-test'.

The workflow is triggered on three types of events:

1) Push Events: The workflow runs whenever there is a push to the main branch. Additionally, it only triggers if the changes are within the src or tests directories. This ensures that the workflow only runs when relevant files are modified, optimizing the build and test process.

2) Pull Request Events: Similar to push events, the workflow is triggered when a pull request is made to the main branch. Again, it only triggers if the changes are within the src or tests directories. This helps in verifying that the changes in the pull request do not break the build or tests before merging into the main branch.

3) Workflow Dispatch: This allows the workflow to be triggered manually via the GitHub Actions interface. 

Define the job:
- Name the job 'job-build-and-test'.
- Set the runner to ubuntu-latest.

Add the steps to the job:
- Checkout the repository:
-- Use the actions/checkout@v4 action.
- Set up Node.js:
-- Use the actions/setup-node@v4 action.
-- Specify the Node.js version (20.9.0).
- Install dependencies with npm for a clean install.
- Run tests

