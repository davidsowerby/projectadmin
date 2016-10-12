package uk.q3c.build.creator.gradle

import com.google.common.base.Splitter
import spock.lang.Specification
import uk.q3c.build.creator.KotlinObjectFactory

/**
 * Created by David Sowerby on 03 Sep 2016
 */
abstract class BlockReaderSpecification extends Specification {


    protected List<String> resultLines() {
        String bufferContent = KotlinObjectFactory.fileBuffer().output()
        return Splitter.on("\n").splitToList(bufferContent)
    }
}
