# Copyright (c) 2003 Entrian Solutions. All rights reserved.
# Released under the Python Software License - see www.python.org

"""Adds the 'goto' and 'comefrom' keywords to Python.

The 'goto' and 'comefrom' keywords add flexibility to Python's control flow
mechanisms, and allow Python programmers to use many common control flow
idioms that were previously denied to them.  Some examples are given below.


*goto*

'goto' jumps the program execution directly to another line of code.  The
target line must be identified using a 'label' statement.  Labels are defined
using the 'label' keyword, and have names which are arbitrary Python
identifiers prefixed with a single dot, like this::

label .myLabel

To jump to a label, use the 'goto' keyword like this::

goto .myLabel


*Computed goto*

You can use a computed goto by assigning the name of the label to a variable
at runtime and referencing it using an asterisk like this::

x = calculateLabelName()
goto *x

The value of 'x' should not include the leading dot.  See Example 5 below
for a full example.


*comefrom*

'comefrom' is the opposite of 'goto'.  It allows a piece of code to say
"Whenever label X is reached, jump to here instead."  For example::

# ...code 1...
label .somewhere
# ...code 2...
comefrom .somewhere

Here, "code 2" will not run - execution will jump directly from the
"label .somewhere" line to the "comefrom .somewhere" line.  'comefrom' is
typically used as a debugging aid - its use in production code is
discouraged since it can lead to surprising behaviour.


*Restrictions*

There are some classes of goto and comefrom which would be unpythonic, and
hence there are some restrictions on where jumps can go:

 o No jumping between modules or functions

 o No jumping into the middle of a loop or a 'finally' clause

 o No jumping onto an 'except' line (because there is no exception)


*Examples*

Here are some examples of how goto and comefrom can be used::

# Example 1: Breaking out from a deeply nested loop:
from goto import goto, label
for i in range(1, 10):
    for j in range(1, 20):
        for k in range(1, 30):
            print i, j, k
            if k == 3:
                goto .end
label .end
print "Finished\n"
 
 
# Example 2: Restarting a loop:
from goto import goto, label
label .start
for i in range(1, 4):
    print i
    if i == 2:
        try:
            output = message
        except NameError:
            print "Oops - forgot to define 'message'!  Start again."
            message = "Hello world"
            goto .start
print output, "\n"
 
 
# Example 3: Cleaning up after something fails:
from goto import goto, label
 
# Imagine that these are real worker functions.
def setUp(): print "setUp"
def doFirstTask(): print 1; return True
def doSecondTask(): print 2; return True
def doThirdTask(): print 3; return False  # This one pretends to fail.
def doFourthTask(): print 4; return True
def cleanUp(): print "cleanUp"
 
# This prints "setUp, 1, 2, 3, cleanUp" - no "4" because doThirdTask fails.
def bigFunction1():
    setUp()
    if not doFirstTask():
        goto .cleanup
    if not doSecondTask():
        goto .cleanup
    if not doThirdTask():
        goto .cleanup
    if not doFourthTask():
        goto .cleanup
 
    label .cleanup
    cleanUp()
 
bigFunction1()
print "bigFunction1 done\n"
 
 
# Example 4: Using comefrom to let the cleanup code take control itself.
from goto import comefrom, label
def bigFunction2():
    setUp()
    if not doFirstTask():
        label .failed
    if not doSecondTask():
        label .failed
    if not doThirdTask():
        label .failed
    if not doFourthTask():
        label .failed
 
    comefrom .failed
    cleanUp()
 
bigFunction2()
print "bigFunction2 done\n"
 
 
# Example 5: Using a computed goto:
from goto import goto, label
 
label .getinput
i = raw_input("Enter either 'a', 'b' or 'c', or any other letter to quit: ")
if i in ('a', 'b', 'c'):
    goto *i
else:
    goto .quit
 
label .a
print "You typed 'a'"
goto .getinput
 
label .b
print "You typed 'b'"
goto .getinput
 
label .c
print "You typed 'c'"
goto .getinput
 
label .quit
print "Finished\n"
 
 
# Example 6: What happens when a label is missing:
from goto import goto, label
label .real
goto .unreal      # Raises a MissingLabelError exception.


This module is released under the Python Software Foundation license, which
can be found at http://www.python.org/  It requires Python 2.3 or later.

Richie Hindle, _richie@entrian.com_

Version 1.0, released 1st April 2004.
"""

__author__ = "Richie Hindle <richie@entrian.com>"
__version__ = 1.0


import sys, token, tokenize

class MissingLabelError(Exception):
    """'goto' without matching 'label'."""
    pass

# Source filenames -> line numbers of plain gotos -> target label names.
_plainGotoCache = {}

# Source filenames -> line numbers of computed gotos -> identifier names.
_computedGotoCache = {}

# Source filenames -> line numbers of labels -> label names.
_labelCache = {}

# Source filenames -> label names -> line numbers of those labels.
_labelNameCache = {}

# Source filenames -> comefrom label names -> line numbers of those comefroms.
_comefromNameCache = {}

def _addToCaches(moduleFilename):
    """Finds the labels and gotos in a module and adds them to the caches."""

    # The token patterns that denote gotos and labels.
    plainGotoPattern = [(token.NAME, 'goto'), (token.OP, '.')]
    computedGotoPattern = [(token.NAME, 'goto'), (token.OP, '*')]
    labelPattern = [(token.NAME, 'label'), (token.OP, '.')]
    comefromPattern = [(token.NAME, 'comefrom'), (token.OP, '.')]

    # Initialise this module's cache entries.
    _plainGotoCache[moduleFilename] = {}
    _computedGotoCache[moduleFilename] = {}
    _labelCache[moduleFilename] = {}
    _labelNameCache[moduleFilename] = {}
    _comefromNameCache[moduleFilename] = {}

    # Tokenize the module; 'window' is the last two (type, string) pairs.
    window = [(None, ''), (None, '')]
    for tokenType, tokenString, (startRow, startCol), (endRow, endCol), line \
            in tokenize.generate_tokens(open(moduleFilename, 'r').readline):
        # Plain goto: "goto .x"
        if window == plainGotoPattern:
            _plainGotoCache[moduleFilename][startRow] = tokenString

        # Computed goto: "goto *identifier"  XXX Allow expressions.
        elif window == computedGotoPattern:
            _computedGotoCache[moduleFilename][startRow] = tokenString

        # Comefrom: "comefrom .x"  XXX Non-determinism via multiple comefroms.
        if window == comefromPattern:
            _comefromNameCache[moduleFilename][tokenString] = startRow

        # Label: "label .x"  XXX Computed labels.
        elif window == labelPattern:
            _labelCache[moduleFilename][startRow] = tokenString
            _labelNameCache[moduleFilename][tokenString] = startRow

        # Move the token window back by one.
        window = [window[1], (tokenType, tokenString)]

def _trace(frame, event, arg):
    # If this is the first time we've seen this source file, cache it.
    filename = frame.f_code.co_filename
    if filename not in _plainGotoCache:
        _addToCaches(filename)

    # Is there a goto on this line?
    targetLabel = _plainGotoCache[filename].get(frame.f_lineno)
    if not targetLabel:
        # No plain goto.  Is there a computed goto?
        identifier = _computedGotoCache[filename].get(frame.f_lineno)
        if identifier:
            # If eval explodes, just let the exception propagate.
            targetLabel = eval(identifier, frame.f_globals, frame.f_locals)

    # Jump to the label's line.
    if targetLabel:
        try:
            targetLine = _labelNameCache[filename][targetLabel]
        except KeyError:
            raise MissingLabelError, "Missing label: %s" % targetLabel
        frame.f_lineno = targetLine

    # Is there a label on this line with a corresponding comefrom?
    label = _labelCache[filename].get(frame.f_lineno)
    if label:
        targetComefromLine = _comefromNameCache[filename].get(label)
        if targetComefromLine:
            frame.f_lineno = targetComefromLine

    return _trace

# Install the trace function, including all preceding frames.
sys.settrace(_trace)
frame = sys._getframe().f_back
while frame:
    frame.f_trace = _trace
    frame = frame.f_back

# Define the so-called keywords for importing: 'goto', 'label' and 'comefrom'.
class _Label:
    """Allows arbitrary x.y attribute lookups."""
    def __getattr__(self, name):
        return None

goto = None
label = _Label()
comefrom = _Label()
