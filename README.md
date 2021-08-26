# Usage

```shell
java -jar path/to/jar --config-file=config.properties
```

or

```shell
java -jar path/to/jar \
    --hosts=host,host, \
    --port=port \
    --user=user \
    --pass=pass \
    --routine=routine
```

or in instances where the routine and credentials are the same but the host changes

```shell
java -jar path/to/jar \
    --config-file=config.properties \
    --hosts=host,host,
```

## Note: command line arguments will override config files
