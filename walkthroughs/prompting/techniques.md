`Zero-Shot prompting` is when you ask for something directly without any examples. Like "Write a factorial function." This works well for straightforward tasks.

`Few-Shot prompting` is when you provide a couple of examples first to show the format or style you want, then ask for a third one. For example, you might show Copilot two examples of how you want error messages formatted, then ask it to format a third one. This is really powerful when you have a specific style or pattern you want to maintain.

`Chain-of-Thought prompting` is when you ask Copilot to think step by step through a problem. Instead of "Fix this bug," you say "Let's debug this step by step. First, identify what the function should do. Second, identify what it's actually doing. Third, explain the discrepancy. Fourth, propose a fix." This works really well for complex logic or debugging.

`Prompt Chaining` is when you do multi-stage tasks where the output of one step becomes the input for the next step. For example: Step 1 - "List all API endpoints in this file." Step 2 - "For each endpoint, generate JSDoc documentation." Step 3 - "Create a README that explains how to use these endpoints."

`Self-Consistency` is when you generate the same thing multiple times and compare results to verify reliability. This is useful for critical code where you want to make sure Copilot is giving you consistent, reliable suggestions.

`Generated Knowledge prompting` is when you ask Copilot to first list what it knows about a topic, then use that knowledge to answer your question. For example: "First, list the key principles of OAuth2 authentication. Then, use those principles to evaluate whether this implementation is secure."
