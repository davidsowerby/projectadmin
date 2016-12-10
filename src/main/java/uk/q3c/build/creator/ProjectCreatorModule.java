package uk.q3c.build.creator;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import uk.q3c.build.creator.dir.DirectoryBuilder;
import uk.q3c.build.creator.gradle.GradleGroovyBuilder;
import uk.q3c.build.gitplus.GitPlusModule;

/**
 * Created by David Sowerby on 10 Oct 2016
 */
public class ProjectCreatorModule extends AbstractModule {

    private Multibinder<Builder> builders;

    @Override
    protected void configure() {
        install(new GitPlusModule());
        builders = Multibinder.newSetBinder(binder(), Builder.class);
        builders.addBinding().toInstance(new GradleGroovyBuilder());
        builders.addBinding().toInstance(new DirectoryBuilder());
        bind(ProjectCreator.class).to(DefaultProjectCreator.class);
        bind(ProjectConfiguration.class).to(DefaultProjectConfiguration.class);

    }
}
