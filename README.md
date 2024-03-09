# Design Documents
https://github.com/acemint/x-admin-backend/docs

# Contributing
## Setup
- Required Java Version JDK 21
- (Optional) - Install podman to run locally

## Commit Convention
THis document outlines how to write commit messages such that it is easier for yourself and others to track what you have changed. 
Adapted from [here](https://gist.github.com/qoomon/5dfcdf8eec66a051ecd85625518cfd13#examples)

### Commit Format:
```
<type>: <description>
\n
<optional body>
\n
<optional footer>
```

### Examples:
```
feat: add email notifications on new direct messages
```

```
feat: remove ticket list endpoint

refers to JIRA-1337

BREAKING CHANGES: ticket enpoints no longer supports list all entites.
```

### Type
Essentially defines the type of change that is made
- `feat` Commits, that adds or remove a new feature
- `fix` Commits, that fixes a bug
- `refactor` Commits, that rewrite/restructure your code, however does not change any API behaviour
- `perf` Commits are special refactor commits, that improve performance
- `style` Commits, that do not affect the meaning (white-space, formatting, missing semi-colons, etc)
- `test` Commits, that add missing tests or correcting existing tests
- `docs` Commits, that affect documentation only
- `build` Commits, that affect build components like build tool, ci pipeline, dependencies, project version, ...
- `ops` Commits, that affect operational components like infrastructure, deployment, backup, recovery, ...
- `chore` Miscellaneous commits e.g. modifying .gitignore

### Description
Describe what changed you made in one sentence

- Use the imperative, present tense: "change" not "changed" nor "changes"
    - Think of This commit will... or This commit should...
- Don't capitalize the first letter
- No dot (.) at the end

### Body (Optional)
The body should include the motivation for the change and contrast this with previous behavior.

- Use the imperative, present tense: "change" not "changed" nor "changes"
- This is the place to mention issue identifiers and their relations (for example, HCN-1)

### Footer (Optional)
The footer should contain any information about Breaking Changes

- Breaking Changes should start with the word BREAKING CHANGES: followed by space or two newlines. The rest of the commit message is then used for this.
