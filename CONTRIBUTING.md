# Contributing to Less URL

First off, thank you for considering contributing to Less URL. It's people like you that make Less URL such a great tool.

## Where do I go from here?

If you've noticed a bug or have a feature request, make sure to check our [Issues](https://github.com/yourusername/less-url/issues) page to see if someone else in the community has already created a ticket. If not, go ahead and [make one](https://github.com/yourusername/less-url/issues/new)!

## Fork & create a branch

If this is something you think you can fix, then fork Less URL and create a branch with a descriptive name.

A good branch name would be (where issue #325 is the ticket you're working on):

```
git checkout -b 325-add-japanese-localization
```

## Get the test suite running

Make sure you're using the correct version of Java (17) and have all dependencies installed:

```
mvn clean install
```

## Implement your fix or feature

At this point, you're ready to make your changes! Feel free to ask for help; everyone is a beginner at first.

## Get the style right

Your patch should follow the same conventions & pass the same code quality checks as the rest of the project. We use checkstyle for Java. You can run it locally with:

```
mvn checkstyle:check
```

## Make a Pull Request

At this point, you should switch back to your master branch and make sure it's up to date with Less URL's master branch:

```
git remote add upstream git@github.com:yourusername/less-url.git
git checkout master
git pull upstream master
```

Then update your feature branch from your local copy of master, and push it!

```
git checkout 325-add-japanese-localization
git rebase master
git push --set-upstream origin 325-add-japanese-localization
```

Finally, go to GitHub and [make a Pull Request](https://github.com/yourusername/less-url/compare) :D

## Keeping your Pull Request updated

If a maintainer asks you to "rebase" your PR, they're saying that a lot of code has changed, and that you need to update your branch so it's easier to merge.

To learn more about rebasing in Git, there are a lot of [good](https://git-scm.com/book/en/v2/Git-Branching-Rebasing) [resources](https://www.atlassian.com/git/tutorials/rewriting-history/git-rebase) but here's the suggested workflow:

```
git checkout 325-add-japanese-localization
git pull --rebase upstream master
git push --force-with-lease 325-add-japanese-localization
```

## Code review

A team member will review your pull request and provide feedback. Please be patient as pull requests are often reviewed in batches.

## That's it!

Thank you for your contribution!