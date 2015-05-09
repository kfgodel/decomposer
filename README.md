Decomposer
==============

Task processor implementation that allows decomposition of current executing task into subtasks

## Typical use case

You want to process a task that requires certain number of steps to be done first, and maybe combine the results of them.  
One option is to use a Java executor. However for very simple subtasks, the cost of thread synchronization is really 
high compared to the cost of the task itself and you also have to deal with asynchronicity.      

A simpler single threaded approach is to use a decomposer processor:

```java  
describe("a decomposer processor", () -> {

  context().decomposer(DecomposerProcessor::create);
  
  it("can process a task with subtasks combining their result",()->{

    AtomicInteger globalCounter = new AtomicInteger(0);

    DecomposableTask nextNumberProducer = (producerTaskContex)-> 
                        String.valueOf(globalCounter.getAndIncrement());

    DecomposableTask aTask = (taskContext)->
      DelayResult
        .waitingFor(nextNumberProducer, nextNumberProducer, nextNumberProducer, nextNumberProducer)
        .andFinally((combinatorContext) -> {
          List<String> subTaskResults = combinatorContext.getSubTaskResults();
          return subTaskResults.stream().collect(Collectors.joining(", "));
        });

    String result = context().decomposer().process(aTask);

    assertThat(result).isEqualTo("0, 1, 2, 3");

  });   

});
```

In this example, a task that produces a list of numbers in a string, is based on several executions of a simpler task 
that generates numbers incrementally. Since in this case we need only four numbers we use four times that sub-task.  
The main task waits for all the results and combines them in a single String.
 
By using this processor the main task is executed first, the sub-tasks later, and then the combination task.  
Any task that uses a DelayedResult as returned object indicates that needs subtasks to be processed first.

Simplify your code by splitting complex processes into simpler tasks, without losing readability or messing with
 concurrency, threading, locks, etc, if they are not really needed.
 
See more complex examples at [DecomposerBehaviorTest](https://github.com/kfgodel/decomposer/blob/master/src/test/java/ar/com/kfgodel/decomposer/DecomposerBehaviorTest.java)
 