# libpricealarm

Java alarms triggered when price of a cryptocurrency or bullion crosses a boundary or surpasses a given change.

Uses [libdynticker](https://github.com/andrefbsantos/libdynticker) to get data from the exchanges.

## Building
Run `mvn process-resources` once to make Maven aware of the plugins we use to download and install dependencies which are not in Maven repositories. For then on you can run `mvn package` to create a jar.

## Versioning
libpricealarm follows [Semantic Versioning](http://semver.org) with the API being the public methods and attributes provided by its classes.

## License and authorship
libpricealarm code licensed under [GNU LGPL v3](/LICENSE) or later. Copyright belongs to [André Filipe Santos](https://github.com/andrefbsantos), [David Ludovino](https://github.com/dllud) and other [contributors listed on GitHub](https://github.com/andrefbsantos/libpricealarm/graphs/contributors).
