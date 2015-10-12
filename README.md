# drools-template-plugin
Maven plugin that generates DRL from template

# Usage

    <plugin>
        <groupId>com.github.bjuvensjo.maven.plugins</groupId>
        <artifactId>drools-template-plugin</artifactId>
        <executions>
            <execution>
                <phase>generate-sources</phase>
                <goals>
                    <goal>generate</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
