package aha.oretama.jp

import groovy.json.JsonBuilder
import org.kohsuke.github.GHIssueState
import org.kohsuke.github.GHPullRequest
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub

/**
 * @author aha-oretama
 * @Date 2017/02/19
 */
@Grapes([
        @Grab(group = 'org.kohsuke', module = 'github-api', version = '1.84'),
        @GrabConfig(systemClassLoader = true)
])
GitHub gitHub = GitHub.connectUsingPassword('XXXX','XXXX')
GHRepository repository = gitHub.getRepository("org/repo")

List<GHPullRequest> pullRequests = repository.getPullRequests(GHIssueState.CLOSED)
        .findAll({ pullRequest -> pullRequest.merged })

def times = pullRequests.collect {[number:it.number, created:it.createdAt, merged:it.mergedAt] }

File file = new File('./pullRequest.json')
if(file.createNewFile()) {

    file.withWriter { writer ->
        new JsonBuilder(times).writeTo(writer)
    }

    println times
}

