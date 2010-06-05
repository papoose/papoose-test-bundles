Papoose Test OSGi Bundles
=====
<em>Test OSGi bundles for project testing.</em>

### org.papoose.test.bundles.test-bundle ###

    <groupId>org.papoose.test.bundles</groupId>
    <artifactId>test-share</artifactId>
    <version>1.0-SNAPSHOT</version>

### org.papoose.test.bundles.test-share ###

A bundle whose bundle activator registers an instance of the `Share` service.  
This service can be used to share key value pairs between bundles.  This can
be useful for test bundles that need to share their internal state when
testing.

    <groupId>org.papoose.test.bundles</groupId>
    <artifactId>test-share</artifactId>
    <version>1.0-SNAPSHOT</version>

### More Resources ###

*  Discuss Papoose at [http://groups.google.com/group/papoose-r4](http://groups.google.com/group/papoose-r4)
*  Learn even more at [http://www.papoose-r4.org](http://www.papoose-r4.org)
*  Check out code at [http://github.com/maguro/papoose-test-bundles](http://github.com/maguro/papoose-test-bundles)
