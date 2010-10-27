# docblock

Create code documentation from `docu` blocks within your function definitons.  Currently very experimental.

## Usage

Use the docblock.doc namespace and add docu blocks to your code.

When you want to document the code, invoke `(docblock.docu/document your-namespace)`.  The documentation is added to the :docblock metadata of your functions.

## Example

    (ns docblock.test-ns
      "A namespace to use in testing docblock"
      (:use docblock.doc))

    (defn f3
      "f3"
      []
      (docu
       "f3"
       (+ 1 (docu "an arg" 2))))

## TODO

- Create some formatters for the resulting documentation graph.
- Generalise to other def forms.

## License

Licensed under [EPL](http://www.eclipse.org/legal/epl-v10.html).

Copyright 2010 Hugo Duncan.
