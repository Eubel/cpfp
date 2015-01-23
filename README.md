# cpfp
Comperative protein function prediction - a Jjva toolkit to deal with [DSSP](http://swift.cmbi.ru.nl/gv/dssp/) data


There are thousands of protein structures in the [Protein Data Bank](http://www.rcsb.org/pdb/home/home.do). The [DSSP-Server](http://swift.cmbi.ru.nl/gv/dssp/) provides secondary structure information about the protein backbone of each chain in the PDB. This algorithm assignes one of the seven structure elements to each amino acid.
These structure elements are:
* H = α-helix
* B = residue in isolated β-bridge
* E = extended strand, participates in β ladder
* G = 3-helix (310 helix)
* I = 5 helix (π-helix)
* T = hydrogen bonded turn
* S = bend

This project aims to access these structure information via a java toolkit.

# Tutorial
[Sample Code](https://github.com/Eubel/cpfp/wiki/Samples)

# external code
We used these third party classes in our project:
* [lambdaj](https://code.google.com/p/lambdaj/) for selecting items in lists
* [DoubleFormatUtil](http://svn.apache.org/viewvc/xmlgraphics/commons/trunk/src/java/org/apache/xmlgraphics/util/DoubleFormatUtil.java?revision=1346428&view=co) for rapid double to string conversion

# authors
* Loos D
* Vogt F
