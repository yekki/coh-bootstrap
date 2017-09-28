Coherence Bootstrap
===================
[![Build Status](https://travis-ci.org/benstopford/coherence-bootstrap.svg?branch=master)](https://travis-ci.org/benstopford/coherence-bootstrap)

**Notice:**

The project is inspired by and based on benstopford's [coherence-bootstrap](https://github.com/benstopford/coherence-bootstrap) which doesn't update for a long time.

Topics covered:

**Basics:**
* [Aggregators](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/basic/Aggregators.java)
* [Cache Stores (Async/Sync)](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/basic/CacheStoreAsync.java)
* [CQCs](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/basic/CQCs.java)
* [Entry Processors](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/basic/EntryProcessors.java)
* [Extend Proxies](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/basic/ExtendProxies.java)
* [Filters](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/basic/Filters.java)
* [Index Performance](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/basic/IndexesAreFast.java)
* [MapListeners](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/basic/MapListeners.java)
* [Near Caching](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/basic/NearCaching.java)
* [PartitionListeners (for detecting data loss)](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/basic/ParitionListenerForDataLoss.java)
* [POF](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/basic/POF.java)
* [Put & Get](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/basic/PutAndGet.java)
* [Triggers](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/basic/Triggers.java)

**More Complex:**
* [Hopping between caches on the server](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/HopBetweenCaches.java)
* [Is POF always a good idea - testing serialisation times](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/PofEfficiency.java)
* [How listeners can lose data](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/ListenersCanLoseData.java)
* [Membership listeners](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/MembershipListeners.java)
* [Multithreaded clients](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/MultiThreadedExtendClientExample.java)
* [PutAll that reports individual errors](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/PutAllThatReportsIndividualExceptions.java)
* [Overflow caches](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/UsingAnOverflowCacheToExpireEntriesToDiskExample.java)
* [Using services to isolate work onto groups of hardware](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/UsingServicesToIsolateWorkOnDifferentSetsOfMachines.java)
* [Sizing the indexes in your Coherence Cache via JMX](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/sizing/CountIndexFootprintOverMultipleCachesViaJmx.java)
* [Exploring POF internal encodings](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/PofInternals.java)
* [Understanding POF performance, when it is faster and when it is not?](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/PofEfficiency.java)
* [Put backups on disk with elastic data](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/PutBackupsOnDiskUsingElasticData.java)
* [Put all data on disk with elastic data](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/PutDataOnDiskUsingElasticData.java)
* [Join, server side, using backing map access and key-association](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/JoinTwoCachesUsingBackingMapAccessAndKeyAssociation.java)


**Utilities**
* ClusterGC: GC all processes incrementally [[test](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/sizing/GarbageCollectWholeCluster.java)] [[code](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/structures/tools/jmx/ClusterGC.java)]
* BinaryCacheSizeCounter: Work out how much data is in your cluster. [[test](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/morecomplex/sizing/CountBinarySizeOfAllObjects.java) [code](https://github.com/yekki/coh-bootstrap/blob/master/src/me/yekki/coh/bootstrap/structures/tools/jmx/BinaryCacheSizeCounter.java)]

**Getting started is simple:**
* Download the zip or clone this repository
* If you wish, run 'ant test' to check everything works ok
* Create a project in your IDE of choice
* Add lib/ & "." to your classpath
* Refer to the ant script if you have any problems
