// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ortools/graph/flow_problem.proto

#ifndef GOOGLE_PROTOBUF_INCLUDED_ortools_2fgraph_2fflow_5fproblem_2eproto
#define GOOGLE_PROTOBUF_INCLUDED_ortools_2fgraph_2fflow_5fproblem_2eproto

#include <limits>
#include <string>

#include <google/protobuf/port_def.inc>
#if PROTOBUF_VERSION < 3013000
#error This file was generated by a newer version of protoc which is
#error incompatible with your Protocol Buffer headers. Please update
#error your headers.
#endif
#if 3013000 < PROTOBUF_MIN_PROTOC_VERSION
#error This file was generated by an older version of protoc which is
#error incompatible with your Protocol Buffer headers. Please
#error regenerate this file with a newer version of protoc.
#endif

#include <google/protobuf/port_undef.inc>
#include <google/protobuf/io/coded_stream.h>
#include <google/protobuf/arena.h>
#include <google/protobuf/arenastring.h>
#include <google/protobuf/generated_message_table_driven.h>
#include <google/protobuf/generated_message_util.h>
#include <google/protobuf/inlined_string_field.h>
#include <google/protobuf/metadata_lite.h>
#include <google/protobuf/generated_message_reflection.h>
#include <google/protobuf/message.h>
#include <google/protobuf/repeated_field.h>  // IWYU pragma: export
#include <google/protobuf/extension_set.h>  // IWYU pragma: export
#include <google/protobuf/generated_enum_reflection.h>
#include <google/protobuf/unknown_field_set.h>
// @@protoc_insertion_point(includes)
#include <google/protobuf/port_def.inc>
#define PROTOBUF_INTERNAL_EXPORT_ortools_2fgraph_2fflow_5fproblem_2eproto
PROTOBUF_NAMESPACE_OPEN
namespace internal {
class AnyMetadata;
}  // namespace internal
PROTOBUF_NAMESPACE_CLOSE

// Internal implementation detail -- do not use these members.
struct TableStruct_ortools_2fgraph_2fflow_5fproblem_2eproto {
  static const ::PROTOBUF_NAMESPACE_ID::internal::ParseTableField entries[]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::AuxiliaryParseTableField aux[]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::ParseTable schema[3]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::FieldMetadata field_metadata[];
  static const ::PROTOBUF_NAMESPACE_ID::internal::SerializationTable serialization_table[];
  static const ::PROTOBUF_NAMESPACE_ID::uint32 offsets[];
};
extern const ::PROTOBUF_NAMESPACE_ID::internal::DescriptorTable descriptor_table_ortools_2fgraph_2fflow_5fproblem_2eproto;
namespace operations_research {
class Arc;
class ArcDefaultTypeInternal;
extern ArcDefaultTypeInternal _Arc_default_instance_;
class FlowModel;
class FlowModelDefaultTypeInternal;
extern FlowModelDefaultTypeInternal _FlowModel_default_instance_;
class Node;
class NodeDefaultTypeInternal;
extern NodeDefaultTypeInternal _Node_default_instance_;
}  // namespace operations_research
PROTOBUF_NAMESPACE_OPEN
template<> ::operations_research::Arc* Arena::CreateMaybeMessage<::operations_research::Arc>(Arena*);
template<> ::operations_research::FlowModel* Arena::CreateMaybeMessage<::operations_research::FlowModel>(Arena*);
template<> ::operations_research::Node* Arena::CreateMaybeMessage<::operations_research::Node>(Arena*);
PROTOBUF_NAMESPACE_CLOSE
namespace operations_research {

enum FlowModel_ProblemType : int {
  FlowModel_ProblemType_LINEAR_SUM_ASSIGNMENT = 0,
  FlowModel_ProblemType_MAX_FLOW = 1,
  FlowModel_ProblemType_MIN_COST_FLOW = 2
};
bool FlowModel_ProblemType_IsValid(int value);
constexpr FlowModel_ProblemType FlowModel_ProblemType_ProblemType_MIN = FlowModel_ProblemType_LINEAR_SUM_ASSIGNMENT;
constexpr FlowModel_ProblemType FlowModel_ProblemType_ProblemType_MAX = FlowModel_ProblemType_MIN_COST_FLOW;
constexpr int FlowModel_ProblemType_ProblemType_ARRAYSIZE = FlowModel_ProblemType_ProblemType_MAX + 1;

const ::PROTOBUF_NAMESPACE_ID::EnumDescriptor* FlowModel_ProblemType_descriptor();
template<typename T>
inline const std::string& FlowModel_ProblemType_Name(T enum_t_value) {
  static_assert(::std::is_same<T, FlowModel_ProblemType>::value ||
    ::std::is_integral<T>::value,
    "Incorrect type passed to function FlowModel_ProblemType_Name.");
  return ::PROTOBUF_NAMESPACE_ID::internal::NameOfEnum(
    FlowModel_ProblemType_descriptor(), enum_t_value);
}
inline bool FlowModel_ProblemType_Parse(
    ::PROTOBUF_NAMESPACE_ID::ConstStringParam name, FlowModel_ProblemType* value) {
  return ::PROTOBUF_NAMESPACE_ID::internal::ParseNamedEnum<FlowModel_ProblemType>(
    FlowModel_ProblemType_descriptor(), name, value);
}
// ===================================================================

class Arc PROTOBUF_FINAL :
    public ::PROTOBUF_NAMESPACE_ID::Message /* @@protoc_insertion_point(class_definition:operations_research.Arc) */ {
 public:
  inline Arc() : Arc(nullptr) {}
  virtual ~Arc();

  Arc(const Arc& from);
  Arc(Arc&& from) noexcept
    : Arc() {
    *this = ::std::move(from);
  }

  inline Arc& operator=(const Arc& from) {
    CopyFrom(from);
    return *this;
  }
  inline Arc& operator=(Arc&& from) noexcept {
    if (GetArena() == from.GetArena()) {
      if (this != &from) InternalSwap(&from);
    } else {
      CopyFrom(from);
    }
    return *this;
  }

  inline const ::PROTOBUF_NAMESPACE_ID::UnknownFieldSet& unknown_fields() const {
    return _internal_metadata_.unknown_fields<::PROTOBUF_NAMESPACE_ID::UnknownFieldSet>(::PROTOBUF_NAMESPACE_ID::UnknownFieldSet::default_instance);
  }
  inline ::PROTOBUF_NAMESPACE_ID::UnknownFieldSet* mutable_unknown_fields() {
    return _internal_metadata_.mutable_unknown_fields<::PROTOBUF_NAMESPACE_ID::UnknownFieldSet>();
  }

  static const ::PROTOBUF_NAMESPACE_ID::Descriptor* descriptor() {
    return GetDescriptor();
  }
  static const ::PROTOBUF_NAMESPACE_ID::Descriptor* GetDescriptor() {
    return GetMetadataStatic().descriptor;
  }
  static const ::PROTOBUF_NAMESPACE_ID::Reflection* GetReflection() {
    return GetMetadataStatic().reflection;
  }
  static const Arc& default_instance();

  static void InitAsDefaultInstance();  // FOR INTERNAL USE ONLY
  static inline const Arc* internal_default_instance() {
    return reinterpret_cast<const Arc*>(
               &_Arc_default_instance_);
  }
  static constexpr int kIndexInFileMessages =
    0;

  friend void swap(Arc& a, Arc& b) {
    a.Swap(&b);
  }
  inline void Swap(Arc* other) {
    if (other == this) return;
    if (GetArena() == other->GetArena()) {
      InternalSwap(other);
    } else {
      ::PROTOBUF_NAMESPACE_ID::internal::GenericSwap(this, other);
    }
  }
  void UnsafeArenaSwap(Arc* other) {
    if (other == this) return;
    GOOGLE_DCHECK(GetArena() == other->GetArena());
    InternalSwap(other);
  }

  // implements Message ----------------------------------------------

  inline Arc* New() const final {
    return CreateMaybeMessage<Arc>(nullptr);
  }

  Arc* New(::PROTOBUF_NAMESPACE_ID::Arena* arena) const final {
    return CreateMaybeMessage<Arc>(arena);
  }
  void CopyFrom(const ::PROTOBUF_NAMESPACE_ID::Message& from) final;
  void MergeFrom(const ::PROTOBUF_NAMESPACE_ID::Message& from) final;
  void CopyFrom(const Arc& from);
  void MergeFrom(const Arc& from);
  PROTOBUF_ATTRIBUTE_REINITIALIZES void Clear() final;
  bool IsInitialized() const final;

  size_t ByteSizeLong() const final;
  const char* _InternalParse(const char* ptr, ::PROTOBUF_NAMESPACE_ID::internal::ParseContext* ctx) final;
  ::PROTOBUF_NAMESPACE_ID::uint8* _InternalSerialize(
      ::PROTOBUF_NAMESPACE_ID::uint8* target, ::PROTOBUF_NAMESPACE_ID::io::EpsCopyOutputStream* stream) const final;
  int GetCachedSize() const final { return _cached_size_.Get(); }

  private:
  inline void SharedCtor();
  inline void SharedDtor();
  void SetCachedSize(int size) const final;
  void InternalSwap(Arc* other);
  friend class ::PROTOBUF_NAMESPACE_ID::internal::AnyMetadata;
  static ::PROTOBUF_NAMESPACE_ID::StringPiece FullMessageName() {
    return "operations_research.Arc";
  }
  protected:
  explicit Arc(::PROTOBUF_NAMESPACE_ID::Arena* arena);
  private:
  static void ArenaDtor(void* object);
  inline void RegisterArenaDtor(::PROTOBUF_NAMESPACE_ID::Arena* arena);
  public:

  ::PROTOBUF_NAMESPACE_ID::Metadata GetMetadata() const final;
  private:
  static ::PROTOBUF_NAMESPACE_ID::Metadata GetMetadataStatic() {
    ::PROTOBUF_NAMESPACE_ID::internal::AssignDescriptors(&::descriptor_table_ortools_2fgraph_2fflow_5fproblem_2eproto);
    return ::descriptor_table_ortools_2fgraph_2fflow_5fproblem_2eproto.file_level_metadata[kIndexInFileMessages];
  }

  public:

  // nested types ----------------------------------------------------

  // accessors -------------------------------------------------------

  enum : int {
    kTailNodeIdFieldNumber = 1,
    kHeadNodeIdFieldNumber = 2,
    kUnitCostFieldNumber = 4,
    kCapacityFieldNumber = 3,
  };
  // optional int64 tail_node_id = 1;
  bool has_tail_node_id() const;
  private:
  bool _internal_has_tail_node_id() const;
  public:
  void clear_tail_node_id();
  ::PROTOBUF_NAMESPACE_ID::int64 tail_node_id() const;
  void set_tail_node_id(::PROTOBUF_NAMESPACE_ID::int64 value);
  private:
  ::PROTOBUF_NAMESPACE_ID::int64 _internal_tail_node_id() const;
  void _internal_set_tail_node_id(::PROTOBUF_NAMESPACE_ID::int64 value);
  public:

  // optional int64 head_node_id = 2;
  bool has_head_node_id() const;
  private:
  bool _internal_has_head_node_id() const;
  public:
  void clear_head_node_id();
  ::PROTOBUF_NAMESPACE_ID::int64 head_node_id() const;
  void set_head_node_id(::PROTOBUF_NAMESPACE_ID::int64 value);
  private:
  ::PROTOBUF_NAMESPACE_ID::int64 _internal_head_node_id() const;
  void _internal_set_head_node_id(::PROTOBUF_NAMESPACE_ID::int64 value);
  public:

  // optional int64 unit_cost = 4 [default = 0];
  bool has_unit_cost() const;
  private:
  bool _internal_has_unit_cost() const;
  public:
  void clear_unit_cost();
  ::PROTOBUF_NAMESPACE_ID::int64 unit_cost() const;
  void set_unit_cost(::PROTOBUF_NAMESPACE_ID::int64 value);
  private:
  ::PROTOBUF_NAMESPACE_ID::int64 _internal_unit_cost() const;
  void _internal_set_unit_cost(::PROTOBUF_NAMESPACE_ID::int64 value);
  public:

  // optional int64 capacity = 3 [default = 1];
  bool has_capacity() const;
  private:
  bool _internal_has_capacity() const;
  public:
  void clear_capacity();
  ::PROTOBUF_NAMESPACE_ID::int64 capacity() const;
  void set_capacity(::PROTOBUF_NAMESPACE_ID::int64 value);
  private:
  ::PROTOBUF_NAMESPACE_ID::int64 _internal_capacity() const;
  void _internal_set_capacity(::PROTOBUF_NAMESPACE_ID::int64 value);
  public:

  // @@protoc_insertion_point(class_scope:operations_research.Arc)
 private:
  class _Internal;

  template <typename T> friend class ::PROTOBUF_NAMESPACE_ID::Arena::InternalHelper;
  typedef void InternalArenaConstructable_;
  typedef void DestructorSkippable_;
  ::PROTOBUF_NAMESPACE_ID::internal::HasBits<1> _has_bits_;
  mutable ::PROTOBUF_NAMESPACE_ID::internal::CachedSize _cached_size_;
  ::PROTOBUF_NAMESPACE_ID::int64 tail_node_id_;
  ::PROTOBUF_NAMESPACE_ID::int64 head_node_id_;
  ::PROTOBUF_NAMESPACE_ID::int64 unit_cost_;
  ::PROTOBUF_NAMESPACE_ID::int64 capacity_;
  friend struct ::TableStruct_ortools_2fgraph_2fflow_5fproblem_2eproto;
};
// -------------------------------------------------------------------

class Node PROTOBUF_FINAL :
    public ::PROTOBUF_NAMESPACE_ID::Message /* @@protoc_insertion_point(class_definition:operations_research.Node) */ {
 public:
  inline Node() : Node(nullptr) {}
  virtual ~Node();

  Node(const Node& from);
  Node(Node&& from) noexcept
    : Node() {
    *this = ::std::move(from);
  }

  inline Node& operator=(const Node& from) {
    CopyFrom(from);
    return *this;
  }
  inline Node& operator=(Node&& from) noexcept {
    if (GetArena() == from.GetArena()) {
      if (this != &from) InternalSwap(&from);
    } else {
      CopyFrom(from);
    }
    return *this;
  }

  inline const ::PROTOBUF_NAMESPACE_ID::UnknownFieldSet& unknown_fields() const {
    return _internal_metadata_.unknown_fields<::PROTOBUF_NAMESPACE_ID::UnknownFieldSet>(::PROTOBUF_NAMESPACE_ID::UnknownFieldSet::default_instance);
  }
  inline ::PROTOBUF_NAMESPACE_ID::UnknownFieldSet* mutable_unknown_fields() {
    return _internal_metadata_.mutable_unknown_fields<::PROTOBUF_NAMESPACE_ID::UnknownFieldSet>();
  }

  static const ::PROTOBUF_NAMESPACE_ID::Descriptor* descriptor() {
    return GetDescriptor();
  }
  static const ::PROTOBUF_NAMESPACE_ID::Descriptor* GetDescriptor() {
    return GetMetadataStatic().descriptor;
  }
  static const ::PROTOBUF_NAMESPACE_ID::Reflection* GetReflection() {
    return GetMetadataStatic().reflection;
  }
  static const Node& default_instance();

  static void InitAsDefaultInstance();  // FOR INTERNAL USE ONLY
  static inline const Node* internal_default_instance() {
    return reinterpret_cast<const Node*>(
               &_Node_default_instance_);
  }
  static constexpr int kIndexInFileMessages =
    1;

  friend void swap(Node& a, Node& b) {
    a.Swap(&b);
  }
  inline void Swap(Node* other) {
    if (other == this) return;
    if (GetArena() == other->GetArena()) {
      InternalSwap(other);
    } else {
      ::PROTOBUF_NAMESPACE_ID::internal::GenericSwap(this, other);
    }
  }
  void UnsafeArenaSwap(Node* other) {
    if (other == this) return;
    GOOGLE_DCHECK(GetArena() == other->GetArena());
    InternalSwap(other);
  }

  // implements Message ----------------------------------------------

  inline Node* New() const final {
    return CreateMaybeMessage<Node>(nullptr);
  }

  Node* New(::PROTOBUF_NAMESPACE_ID::Arena* arena) const final {
    return CreateMaybeMessage<Node>(arena);
  }
  void CopyFrom(const ::PROTOBUF_NAMESPACE_ID::Message& from) final;
  void MergeFrom(const ::PROTOBUF_NAMESPACE_ID::Message& from) final;
  void CopyFrom(const Node& from);
  void MergeFrom(const Node& from);
  PROTOBUF_ATTRIBUTE_REINITIALIZES void Clear() final;
  bool IsInitialized() const final;

  size_t ByteSizeLong() const final;
  const char* _InternalParse(const char* ptr, ::PROTOBUF_NAMESPACE_ID::internal::ParseContext* ctx) final;
  ::PROTOBUF_NAMESPACE_ID::uint8* _InternalSerialize(
      ::PROTOBUF_NAMESPACE_ID::uint8* target, ::PROTOBUF_NAMESPACE_ID::io::EpsCopyOutputStream* stream) const final;
  int GetCachedSize() const final { return _cached_size_.Get(); }

  private:
  inline void SharedCtor();
  inline void SharedDtor();
  void SetCachedSize(int size) const final;
  void InternalSwap(Node* other);
  friend class ::PROTOBUF_NAMESPACE_ID::internal::AnyMetadata;
  static ::PROTOBUF_NAMESPACE_ID::StringPiece FullMessageName() {
    return "operations_research.Node";
  }
  protected:
  explicit Node(::PROTOBUF_NAMESPACE_ID::Arena* arena);
  private:
  static void ArenaDtor(void* object);
  inline void RegisterArenaDtor(::PROTOBUF_NAMESPACE_ID::Arena* arena);
  public:

  ::PROTOBUF_NAMESPACE_ID::Metadata GetMetadata() const final;
  private:
  static ::PROTOBUF_NAMESPACE_ID::Metadata GetMetadataStatic() {
    ::PROTOBUF_NAMESPACE_ID::internal::AssignDescriptors(&::descriptor_table_ortools_2fgraph_2fflow_5fproblem_2eproto);
    return ::descriptor_table_ortools_2fgraph_2fflow_5fproblem_2eproto.file_level_metadata[kIndexInFileMessages];
  }

  public:

  // nested types ----------------------------------------------------

  // accessors -------------------------------------------------------

  enum : int {
    kIdFieldNumber = 1,
    kSupplyFieldNumber = 2,
  };
  // optional int64 id = 1;
  bool has_id() const;
  private:
  bool _internal_has_id() const;
  public:
  void clear_id();
  ::PROTOBUF_NAMESPACE_ID::int64 id() const;
  void set_id(::PROTOBUF_NAMESPACE_ID::int64 value);
  private:
  ::PROTOBUF_NAMESPACE_ID::int64 _internal_id() const;
  void _internal_set_id(::PROTOBUF_NAMESPACE_ID::int64 value);
  public:

  // optional int64 supply = 2 [default = 0];
  bool has_supply() const;
  private:
  bool _internal_has_supply() const;
  public:
  void clear_supply();
  ::PROTOBUF_NAMESPACE_ID::int64 supply() const;
  void set_supply(::PROTOBUF_NAMESPACE_ID::int64 value);
  private:
  ::PROTOBUF_NAMESPACE_ID::int64 _internal_supply() const;
  void _internal_set_supply(::PROTOBUF_NAMESPACE_ID::int64 value);
  public:

  // @@protoc_insertion_point(class_scope:operations_research.Node)
 private:
  class _Internal;

  template <typename T> friend class ::PROTOBUF_NAMESPACE_ID::Arena::InternalHelper;
  typedef void InternalArenaConstructable_;
  typedef void DestructorSkippable_;
  ::PROTOBUF_NAMESPACE_ID::internal::HasBits<1> _has_bits_;
  mutable ::PROTOBUF_NAMESPACE_ID::internal::CachedSize _cached_size_;
  ::PROTOBUF_NAMESPACE_ID::int64 id_;
  ::PROTOBUF_NAMESPACE_ID::int64 supply_;
  friend struct ::TableStruct_ortools_2fgraph_2fflow_5fproblem_2eproto;
};
// -------------------------------------------------------------------

class FlowModel PROTOBUF_FINAL :
    public ::PROTOBUF_NAMESPACE_ID::Message /* @@protoc_insertion_point(class_definition:operations_research.FlowModel) */ {
 public:
  inline FlowModel() : FlowModel(nullptr) {}
  virtual ~FlowModel();

  FlowModel(const FlowModel& from);
  FlowModel(FlowModel&& from) noexcept
    : FlowModel() {
    *this = ::std::move(from);
  }

  inline FlowModel& operator=(const FlowModel& from) {
    CopyFrom(from);
    return *this;
  }
  inline FlowModel& operator=(FlowModel&& from) noexcept {
    if (GetArena() == from.GetArena()) {
      if (this != &from) InternalSwap(&from);
    } else {
      CopyFrom(from);
    }
    return *this;
  }

  inline const ::PROTOBUF_NAMESPACE_ID::UnknownFieldSet& unknown_fields() const {
    return _internal_metadata_.unknown_fields<::PROTOBUF_NAMESPACE_ID::UnknownFieldSet>(::PROTOBUF_NAMESPACE_ID::UnknownFieldSet::default_instance);
  }
  inline ::PROTOBUF_NAMESPACE_ID::UnknownFieldSet* mutable_unknown_fields() {
    return _internal_metadata_.mutable_unknown_fields<::PROTOBUF_NAMESPACE_ID::UnknownFieldSet>();
  }

  static const ::PROTOBUF_NAMESPACE_ID::Descriptor* descriptor() {
    return GetDescriptor();
  }
  static const ::PROTOBUF_NAMESPACE_ID::Descriptor* GetDescriptor() {
    return GetMetadataStatic().descriptor;
  }
  static const ::PROTOBUF_NAMESPACE_ID::Reflection* GetReflection() {
    return GetMetadataStatic().reflection;
  }
  static const FlowModel& default_instance();

  static void InitAsDefaultInstance();  // FOR INTERNAL USE ONLY
  static inline const FlowModel* internal_default_instance() {
    return reinterpret_cast<const FlowModel*>(
               &_FlowModel_default_instance_);
  }
  static constexpr int kIndexInFileMessages =
    2;

  friend void swap(FlowModel& a, FlowModel& b) {
    a.Swap(&b);
  }
  inline void Swap(FlowModel* other) {
    if (other == this) return;
    if (GetArena() == other->GetArena()) {
      InternalSwap(other);
    } else {
      ::PROTOBUF_NAMESPACE_ID::internal::GenericSwap(this, other);
    }
  }
  void UnsafeArenaSwap(FlowModel* other) {
    if (other == this) return;
    GOOGLE_DCHECK(GetArena() == other->GetArena());
    InternalSwap(other);
  }

  // implements Message ----------------------------------------------

  inline FlowModel* New() const final {
    return CreateMaybeMessage<FlowModel>(nullptr);
  }

  FlowModel* New(::PROTOBUF_NAMESPACE_ID::Arena* arena) const final {
    return CreateMaybeMessage<FlowModel>(arena);
  }
  void CopyFrom(const ::PROTOBUF_NAMESPACE_ID::Message& from) final;
  void MergeFrom(const ::PROTOBUF_NAMESPACE_ID::Message& from) final;
  void CopyFrom(const FlowModel& from);
  void MergeFrom(const FlowModel& from);
  PROTOBUF_ATTRIBUTE_REINITIALIZES void Clear() final;
  bool IsInitialized() const final;

  size_t ByteSizeLong() const final;
  const char* _InternalParse(const char* ptr, ::PROTOBUF_NAMESPACE_ID::internal::ParseContext* ctx) final;
  ::PROTOBUF_NAMESPACE_ID::uint8* _InternalSerialize(
      ::PROTOBUF_NAMESPACE_ID::uint8* target, ::PROTOBUF_NAMESPACE_ID::io::EpsCopyOutputStream* stream) const final;
  int GetCachedSize() const final { return _cached_size_.Get(); }

  private:
  inline void SharedCtor();
  inline void SharedDtor();
  void SetCachedSize(int size) const final;
  void InternalSwap(FlowModel* other);
  friend class ::PROTOBUF_NAMESPACE_ID::internal::AnyMetadata;
  static ::PROTOBUF_NAMESPACE_ID::StringPiece FullMessageName() {
    return "operations_research.FlowModel";
  }
  protected:
  explicit FlowModel(::PROTOBUF_NAMESPACE_ID::Arena* arena);
  private:
  static void ArenaDtor(void* object);
  inline void RegisterArenaDtor(::PROTOBUF_NAMESPACE_ID::Arena* arena);
  public:

  ::PROTOBUF_NAMESPACE_ID::Metadata GetMetadata() const final;
  private:
  static ::PROTOBUF_NAMESPACE_ID::Metadata GetMetadataStatic() {
    ::PROTOBUF_NAMESPACE_ID::internal::AssignDescriptors(&::descriptor_table_ortools_2fgraph_2fflow_5fproblem_2eproto);
    return ::descriptor_table_ortools_2fgraph_2fflow_5fproblem_2eproto.file_level_metadata[kIndexInFileMessages];
  }

  public:

  // nested types ----------------------------------------------------

  typedef FlowModel_ProblemType ProblemType;
  static constexpr ProblemType LINEAR_SUM_ASSIGNMENT =
    FlowModel_ProblemType_LINEAR_SUM_ASSIGNMENT;
  static constexpr ProblemType MAX_FLOW =
    FlowModel_ProblemType_MAX_FLOW;
  static constexpr ProblemType MIN_COST_FLOW =
    FlowModel_ProblemType_MIN_COST_FLOW;
  static inline bool ProblemType_IsValid(int value) {
    return FlowModel_ProblemType_IsValid(value);
  }
  static constexpr ProblemType ProblemType_MIN =
    FlowModel_ProblemType_ProblemType_MIN;
  static constexpr ProblemType ProblemType_MAX =
    FlowModel_ProblemType_ProblemType_MAX;
  static constexpr int ProblemType_ARRAYSIZE =
    FlowModel_ProblemType_ProblemType_ARRAYSIZE;
  static inline const ::PROTOBUF_NAMESPACE_ID::EnumDescriptor*
  ProblemType_descriptor() {
    return FlowModel_ProblemType_descriptor();
  }
  template<typename T>
  static inline const std::string& ProblemType_Name(T enum_t_value) {
    static_assert(::std::is_same<T, ProblemType>::value ||
      ::std::is_integral<T>::value,
      "Incorrect type passed to function ProblemType_Name.");
    return FlowModel_ProblemType_Name(enum_t_value);
  }
  static inline bool ProblemType_Parse(::PROTOBUF_NAMESPACE_ID::ConstStringParam name,
      ProblemType* value) {
    return FlowModel_ProblemType_Parse(name, value);
  }

  // accessors -------------------------------------------------------

  enum : int {
    kNodeFieldNumber = 1,
    kArcFieldNumber = 2,
    kProblemTypeFieldNumber = 3,
  };
  // repeated .operations_research.Node node = 1;
  int node_size() const;
  private:
  int _internal_node_size() const;
  public:
  void clear_node();
  ::operations_research::Node* mutable_node(int index);
  ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField< ::operations_research::Node >*
      mutable_node();
  private:
  const ::operations_research::Node& _internal_node(int index) const;
  ::operations_research::Node* _internal_add_node();
  public:
  const ::operations_research::Node& node(int index) const;
  ::operations_research::Node* add_node();
  const ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField< ::operations_research::Node >&
      node() const;

  // repeated .operations_research.Arc arc = 2;
  int arc_size() const;
  private:
  int _internal_arc_size() const;
  public:
  void clear_arc();
  ::operations_research::Arc* mutable_arc(int index);
  ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField< ::operations_research::Arc >*
      mutable_arc();
  private:
  const ::operations_research::Arc& _internal_arc(int index) const;
  ::operations_research::Arc* _internal_add_arc();
  public:
  const ::operations_research::Arc& arc(int index) const;
  ::operations_research::Arc* add_arc();
  const ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField< ::operations_research::Arc >&
      arc() const;

  // optional .operations_research.FlowModel.ProblemType problem_type = 3 [default = MIN_COST_FLOW];
  bool has_problem_type() const;
  private:
  bool _internal_has_problem_type() const;
  public:
  void clear_problem_type();
  ::operations_research::FlowModel_ProblemType problem_type() const;
  void set_problem_type(::operations_research::FlowModel_ProblemType value);
  private:
  ::operations_research::FlowModel_ProblemType _internal_problem_type() const;
  void _internal_set_problem_type(::operations_research::FlowModel_ProblemType value);
  public:

  // @@protoc_insertion_point(class_scope:operations_research.FlowModel)
 private:
  class _Internal;

  template <typename T> friend class ::PROTOBUF_NAMESPACE_ID::Arena::InternalHelper;
  typedef void InternalArenaConstructable_;
  typedef void DestructorSkippable_;
  ::PROTOBUF_NAMESPACE_ID::internal::HasBits<1> _has_bits_;
  mutable ::PROTOBUF_NAMESPACE_ID::internal::CachedSize _cached_size_;
  ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField< ::operations_research::Node > node_;
  ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField< ::operations_research::Arc > arc_;
  int problem_type_;
  friend struct ::TableStruct_ortools_2fgraph_2fflow_5fproblem_2eproto;
};
// ===================================================================


// ===================================================================

#ifdef __GNUC__
  #pragma GCC diagnostic push
  #pragma GCC diagnostic ignored "-Wstrict-aliasing"
#endif  // __GNUC__
// Arc

// optional int64 tail_node_id = 1;
inline bool Arc::_internal_has_tail_node_id() const {
  bool value = (_has_bits_[0] & 0x00000001u) != 0;
  return value;
}
inline bool Arc::has_tail_node_id() const {
  return _internal_has_tail_node_id();
}
inline void Arc::clear_tail_node_id() {
  tail_node_id_ = PROTOBUF_LONGLONG(0);
  _has_bits_[0] &= ~0x00000001u;
}
inline ::PROTOBUF_NAMESPACE_ID::int64 Arc::_internal_tail_node_id() const {
  return tail_node_id_;
}
inline ::PROTOBUF_NAMESPACE_ID::int64 Arc::tail_node_id() const {
  // @@protoc_insertion_point(field_get:operations_research.Arc.tail_node_id)
  return _internal_tail_node_id();
}
inline void Arc::_internal_set_tail_node_id(::PROTOBUF_NAMESPACE_ID::int64 value) {
  _has_bits_[0] |= 0x00000001u;
  tail_node_id_ = value;
}
inline void Arc::set_tail_node_id(::PROTOBUF_NAMESPACE_ID::int64 value) {
  _internal_set_tail_node_id(value);
  // @@protoc_insertion_point(field_set:operations_research.Arc.tail_node_id)
}

// optional int64 head_node_id = 2;
inline bool Arc::_internal_has_head_node_id() const {
  bool value = (_has_bits_[0] & 0x00000002u) != 0;
  return value;
}
inline bool Arc::has_head_node_id() const {
  return _internal_has_head_node_id();
}
inline void Arc::clear_head_node_id() {
  head_node_id_ = PROTOBUF_LONGLONG(0);
  _has_bits_[0] &= ~0x00000002u;
}
inline ::PROTOBUF_NAMESPACE_ID::int64 Arc::_internal_head_node_id() const {
  return head_node_id_;
}
inline ::PROTOBUF_NAMESPACE_ID::int64 Arc::head_node_id() const {
  // @@protoc_insertion_point(field_get:operations_research.Arc.head_node_id)
  return _internal_head_node_id();
}
inline void Arc::_internal_set_head_node_id(::PROTOBUF_NAMESPACE_ID::int64 value) {
  _has_bits_[0] |= 0x00000002u;
  head_node_id_ = value;
}
inline void Arc::set_head_node_id(::PROTOBUF_NAMESPACE_ID::int64 value) {
  _internal_set_head_node_id(value);
  // @@protoc_insertion_point(field_set:operations_research.Arc.head_node_id)
}

// optional int64 capacity = 3 [default = 1];
inline bool Arc::_internal_has_capacity() const {
  bool value = (_has_bits_[0] & 0x00000008u) != 0;
  return value;
}
inline bool Arc::has_capacity() const {
  return _internal_has_capacity();
}
inline void Arc::clear_capacity() {
  capacity_ = PROTOBUF_LONGLONG(1);
  _has_bits_[0] &= ~0x00000008u;
}
inline ::PROTOBUF_NAMESPACE_ID::int64 Arc::_internal_capacity() const {
  return capacity_;
}
inline ::PROTOBUF_NAMESPACE_ID::int64 Arc::capacity() const {
  // @@protoc_insertion_point(field_get:operations_research.Arc.capacity)
  return _internal_capacity();
}
inline void Arc::_internal_set_capacity(::PROTOBUF_NAMESPACE_ID::int64 value) {
  _has_bits_[0] |= 0x00000008u;
  capacity_ = value;
}
inline void Arc::set_capacity(::PROTOBUF_NAMESPACE_ID::int64 value) {
  _internal_set_capacity(value);
  // @@protoc_insertion_point(field_set:operations_research.Arc.capacity)
}

// optional int64 unit_cost = 4 [default = 0];
inline bool Arc::_internal_has_unit_cost() const {
  bool value = (_has_bits_[0] & 0x00000004u) != 0;
  return value;
}
inline bool Arc::has_unit_cost() const {
  return _internal_has_unit_cost();
}
inline void Arc::clear_unit_cost() {
  unit_cost_ = PROTOBUF_LONGLONG(0);
  _has_bits_[0] &= ~0x00000004u;
}
inline ::PROTOBUF_NAMESPACE_ID::int64 Arc::_internal_unit_cost() const {
  return unit_cost_;
}
inline ::PROTOBUF_NAMESPACE_ID::int64 Arc::unit_cost() const {
  // @@protoc_insertion_point(field_get:operations_research.Arc.unit_cost)
  return _internal_unit_cost();
}
inline void Arc::_internal_set_unit_cost(::PROTOBUF_NAMESPACE_ID::int64 value) {
  _has_bits_[0] |= 0x00000004u;
  unit_cost_ = value;
}
inline void Arc::set_unit_cost(::PROTOBUF_NAMESPACE_ID::int64 value) {
  _internal_set_unit_cost(value);
  // @@protoc_insertion_point(field_set:operations_research.Arc.unit_cost)
}

// -------------------------------------------------------------------

// Node

// optional int64 id = 1;
inline bool Node::_internal_has_id() const {
  bool value = (_has_bits_[0] & 0x00000001u) != 0;
  return value;
}
inline bool Node::has_id() const {
  return _internal_has_id();
}
inline void Node::clear_id() {
  id_ = PROTOBUF_LONGLONG(0);
  _has_bits_[0] &= ~0x00000001u;
}
inline ::PROTOBUF_NAMESPACE_ID::int64 Node::_internal_id() const {
  return id_;
}
inline ::PROTOBUF_NAMESPACE_ID::int64 Node::id() const {
  // @@protoc_insertion_point(field_get:operations_research.Node.id)
  return _internal_id();
}
inline void Node::_internal_set_id(::PROTOBUF_NAMESPACE_ID::int64 value) {
  _has_bits_[0] |= 0x00000001u;
  id_ = value;
}
inline void Node::set_id(::PROTOBUF_NAMESPACE_ID::int64 value) {
  _internal_set_id(value);
  // @@protoc_insertion_point(field_set:operations_research.Node.id)
}

// optional int64 supply = 2 [default = 0];
inline bool Node::_internal_has_supply() const {
  bool value = (_has_bits_[0] & 0x00000002u) != 0;
  return value;
}
inline bool Node::has_supply() const {
  return _internal_has_supply();
}
inline void Node::clear_supply() {
  supply_ = PROTOBUF_LONGLONG(0);
  _has_bits_[0] &= ~0x00000002u;
}
inline ::PROTOBUF_NAMESPACE_ID::int64 Node::_internal_supply() const {
  return supply_;
}
inline ::PROTOBUF_NAMESPACE_ID::int64 Node::supply() const {
  // @@protoc_insertion_point(field_get:operations_research.Node.supply)
  return _internal_supply();
}
inline void Node::_internal_set_supply(::PROTOBUF_NAMESPACE_ID::int64 value) {
  _has_bits_[0] |= 0x00000002u;
  supply_ = value;
}
inline void Node::set_supply(::PROTOBUF_NAMESPACE_ID::int64 value) {
  _internal_set_supply(value);
  // @@protoc_insertion_point(field_set:operations_research.Node.supply)
}

// -------------------------------------------------------------------

// FlowModel

// repeated .operations_research.Node node = 1;
inline int FlowModel::_internal_node_size() const {
  return node_.size();
}
inline int FlowModel::node_size() const {
  return _internal_node_size();
}
inline void FlowModel::clear_node() {
  node_.Clear();
}
inline ::operations_research::Node* FlowModel::mutable_node(int index) {
  // @@protoc_insertion_point(field_mutable:operations_research.FlowModel.node)
  return node_.Mutable(index);
}
inline ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField< ::operations_research::Node >*
FlowModel::mutable_node() {
  // @@protoc_insertion_point(field_mutable_list:operations_research.FlowModel.node)
  return &node_;
}
inline const ::operations_research::Node& FlowModel::_internal_node(int index) const {
  return node_.Get(index);
}
inline const ::operations_research::Node& FlowModel::node(int index) const {
  // @@protoc_insertion_point(field_get:operations_research.FlowModel.node)
  return _internal_node(index);
}
inline ::operations_research::Node* FlowModel::_internal_add_node() {
  return node_.Add();
}
inline ::operations_research::Node* FlowModel::add_node() {
  // @@protoc_insertion_point(field_add:operations_research.FlowModel.node)
  return _internal_add_node();
}
inline const ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField< ::operations_research::Node >&
FlowModel::node() const {
  // @@protoc_insertion_point(field_list:operations_research.FlowModel.node)
  return node_;
}

// repeated .operations_research.Arc arc = 2;
inline int FlowModel::_internal_arc_size() const {
  return arc_.size();
}
inline int FlowModel::arc_size() const {
  return _internal_arc_size();
}
inline void FlowModel::clear_arc() {
  arc_.Clear();
}
inline ::operations_research::Arc* FlowModel::mutable_arc(int index) {
  // @@protoc_insertion_point(field_mutable:operations_research.FlowModel.arc)
  return arc_.Mutable(index);
}
inline ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField< ::operations_research::Arc >*
FlowModel::mutable_arc() {
  // @@protoc_insertion_point(field_mutable_list:operations_research.FlowModel.arc)
  return &arc_;
}
inline const ::operations_research::Arc& FlowModel::_internal_arc(int index) const {
  return arc_.Get(index);
}
inline const ::operations_research::Arc& FlowModel::arc(int index) const {
  // @@protoc_insertion_point(field_get:operations_research.FlowModel.arc)
  return _internal_arc(index);
}
inline ::operations_research::Arc* FlowModel::_internal_add_arc() {
  return arc_.Add();
}
inline ::operations_research::Arc* FlowModel::add_arc() {
  // @@protoc_insertion_point(field_add:operations_research.FlowModel.arc)
  return _internal_add_arc();
}
inline const ::PROTOBUF_NAMESPACE_ID::RepeatedPtrField< ::operations_research::Arc >&
FlowModel::arc() const {
  // @@protoc_insertion_point(field_list:operations_research.FlowModel.arc)
  return arc_;
}

// optional .operations_research.FlowModel.ProblemType problem_type = 3 [default = MIN_COST_FLOW];
inline bool FlowModel::_internal_has_problem_type() const {
  bool value = (_has_bits_[0] & 0x00000001u) != 0;
  return value;
}
inline bool FlowModel::has_problem_type() const {
  return _internal_has_problem_type();
}
inline void FlowModel::clear_problem_type() {
  problem_type_ = 2;
  _has_bits_[0] &= ~0x00000001u;
}
inline ::operations_research::FlowModel_ProblemType FlowModel::_internal_problem_type() const {
  return static_cast< ::operations_research::FlowModel_ProblemType >(problem_type_);
}
inline ::operations_research::FlowModel_ProblemType FlowModel::problem_type() const {
  // @@protoc_insertion_point(field_get:operations_research.FlowModel.problem_type)
  return _internal_problem_type();
}
inline void FlowModel::_internal_set_problem_type(::operations_research::FlowModel_ProblemType value) {
  assert(::operations_research::FlowModel_ProblemType_IsValid(value));
  _has_bits_[0] |= 0x00000001u;
  problem_type_ = value;
}
inline void FlowModel::set_problem_type(::operations_research::FlowModel_ProblemType value) {
  _internal_set_problem_type(value);
  // @@protoc_insertion_point(field_set:operations_research.FlowModel.problem_type)
}

#ifdef __GNUC__
  #pragma GCC diagnostic pop
#endif  // __GNUC__
// -------------------------------------------------------------------

// -------------------------------------------------------------------


// @@protoc_insertion_point(namespace_scope)

}  // namespace operations_research

PROTOBUF_NAMESPACE_OPEN

template <> struct is_proto_enum< ::operations_research::FlowModel_ProblemType> : ::std::true_type {};
template <>
inline const EnumDescriptor* GetEnumDescriptor< ::operations_research::FlowModel_ProblemType>() {
  return ::operations_research::FlowModel_ProblemType_descriptor();
}

PROTOBUF_NAMESPACE_CLOSE

// @@protoc_insertion_point(global_scope)

#include <google/protobuf/port_undef.inc>
#endif  // GOOGLE_PROTOBUF_INCLUDED_GOOGLE_PROTOBUF_INCLUDED_ortools_2fgraph_2fflow_5fproblem_2eproto
