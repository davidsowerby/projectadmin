package uk.q3c.build.creator.gradle

import com.google.common.base.Splitter
import com.google.common.collect.ImmutableList
import spock.lang.Specification
import uk.q3c.build.creator.KotlinObjectFactory
import uk.q3c.build.creator.gradle.buffer.FileBuffer

/**
 * Created by David Sowerby on 03 Sep 2016
 */
abstract class BlockReaderSpecification extends Specification {

    FileBuffer fileBuffer

    protected List<String> resultLines() {
        String bufferContent = KotlinObjectFactory.fileBuffer().output()
        return ImmutableList.copyOf(Splitter.on("\n").splitToList(bufferContent))
    }
}


