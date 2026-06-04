# About

This repo shows how the 'PreLaunchEntrypoint' in Fabric can be used for arbitrary class transformation.
The project currently shifts static <clinit> initializers into a public static method that can be invoked again.
There are many use cases for a setup like this, reverse engineering, advanced transformation beyond mixin capabilities and ASM magic.

## Instructions

Build the 'dummy-mod' subproject
Move the built dummy-mod to the root project's runClient mod directory
Run `gradle runClient`
Observe logs
Profit???

## Extended usage

A good starting point to adding your own behavior, is to edit:
`me.sootysplash.swap.clinitInvoker.OurMixinTransformer.transformClassBytes(String name, String transformedName, byte[] basicClass)`
From there, you can change targets and transformations, after that, edit:
`me.sootysplash.swap.clinitInvoker.OnInit#onInitialize()`
To add your own tests.
