# Guitar tab generator

## Introduction

The aim is to develop a Java application for writing guitar tabs easily and quickly.

Instead of specifying the duration of the note, I find it easier to write down when the note should be played.
The way in which the tab is written follows this philosophy. The tab generator is in no way complete, but it is
useful for quicker generating memory-jogging tabs and for transposing to different keys.

## Usage

There is a simple command line interface (CLI) to:

- Build a text file containing the guitar tab from a specification file
- Transpose the music
- Open a web browser on a video in a given musical key

### Configuration

The config.properties file contains configuration parameters that need to be set:

- page.width=<number of characters>

### Usage examples

In general:

```
java -jar guitartabgenerator.jar -spec <file> -tab <file> -transpose <key> -video
```

To launch the the video in the 'standard' key:

```
java -jar guitartabgenerator.jar -spec <file> -video
```

To launch the video in the required key:

```
java -jar guitartabgenerator.jar -spec <file> -transpose <key> -video
```

## Example

Here is the specification file for a song:

```
<Add song example>
```


The generated tab looks like this:

```
<Add generated tab>
```

## TODO

- Implement tab building in 6/8 timing.
