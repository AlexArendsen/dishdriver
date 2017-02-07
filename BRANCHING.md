# Branching And Versioning

This document proposes a branching structure for our project, and a simplified
semantic versioning scheme.

## Branching

* `master`
* `ui-master`
* `ui-development`
* `lib-master`
* `lib-development`

`ui-master` and `lib-master` should contain stable versions of their respective
components at all times. `ui-development` and `lib-development`, on the other
hand, do not have to be stable. The `master` branch should contain the most
recent releases from both subcomponents.

This setup allows the UI and the library to have compatible versions of one
another, and prevents breakage in one component from preventing the other
component from being effectively developed.

To achieve meaningful merges into the master branches, semantic versioning
should be used.

## Versioning

The following scheme is proposed:

    MAJOR.RELEASE.REVISION

Major version 0 is reserved for initial development. Subsequent increments of
the major version represent non-backward-compatible changes, such as those
corresponding to a rewrite or a major API specificaiton revision.

The release version number is incremented with each release. Releases may be
made at any time, as long as the code is believed to be stable and error-free
at the time of the release.

The revision version is incremented each time a logical chunk of changes have
been committed. A note should be added to the changelog corresponding to this
increment. For our project, we could probably make this optional and leave out
the changelog.
