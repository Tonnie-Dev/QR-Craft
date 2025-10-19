package com.tonyxlab.qrcraft.domain.exception

class ItemNotFoundException( id: Long): Exception("Item with Id: $id not found")