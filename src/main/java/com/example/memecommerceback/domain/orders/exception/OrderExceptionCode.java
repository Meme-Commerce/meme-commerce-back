package com.example.memecommerceback.domain.orders.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderExceptionCode {

  STOCK_LOCK_FAILED(
      HttpStatus.BAD_REQUEST, "ORDER-001",
      "해당 상품의 재고를 확보할 수 없습니다. 다시 시도해주세요."),
  NOT_FOUND(
      HttpStatus.BAD_REQUEST, "ORDER-002",
      "해당 상품을 찾을 수 없습니다."),
  NOT_OWNER(
      HttpStatus.BAD_REQUEST, "ORDER-003",
      "해당 상품을 주문한 사람이 아닙니다."),
  NOT_ORDER_PRODUCT(
      HttpStatus.BAD_REQUEST, "ORDER-004",
      "해당 상품을 주문하지 않았습니다."),
  NOT_UPDATE_QUANTITY(
      HttpStatus.BAD_REQUEST, "ORDER-005",
      "해당 상품의 환불 체크 시에는 수량을 설정할 수 없습니다."),
  NOT_UPDATE_STATUS_CASE_CHANGE_QUANTITY(
      HttpStatus.BAD_REQUEST, "ORDER-006",
      "해당 상품의 수량을 수정할 때는 상태를 수정할 수 없습니다."),
  NOT_EXIST_STATUS(
      HttpStatus.BAD_REQUEST, "ORDER-007",
      "해당 상품의 상태는 존재하지 않습니다."),
  QUANTITY_MUST_BE_POSITIVE_AND_DIFFERENT(
      HttpStatus.BAD_REQUEST, "ORDER-008",
      "동일 수량 또는 적절치 않는 주문 수량을 요청하셨습니다."),
  REQUEST_SAME_STATUS(
      HttpStatus.BAD_REQUEST, "ORDER-009",
      "현재 주문 상태와 요청하신 주문 상태와 같습니다."),
  ALREADY_REQUEST(
      HttpStatus.BAD_REQUEST, "ORDER-010",
      "이미 배송 중이거나, 주문 상품 처리 중입니다."),
  ALREADY_COMPLETED_PRODUCT(
      HttpStatus.BAD_REQUEST, "ORDER-011",
      "이미 처리된 상품입니다."),
  INPUT_INVALID_NUMBER(
      HttpStatus.BAD_REQUEST, "ORDER-012",
      "가격 및 수량에 유효한 값을 입력해주세요."),
  NOT_PENDING_STATUS(
      HttpStatus.BAD_REQUEST, "ORDER-013",
      "검수 중인 주문이 아닙니다."),
  INPUT_INVALID_QUANTITY(
      HttpStatus.BAD_REQUEST, "ORDER-014",
      "주문 수량은 0이거나 음수 일 수 없습니다."),
  NEED_TO_CANCELED_REASON(
      HttpStatus.BAD_REQUEST, "ORDER-015",
      "주문을 취소하실 때, 취소 이유는 필수 입력란입니다."),
  CANNOT_UPDATE_COMPLETED_STATUS(
      HttpStatus.BAD_REQUEST, "ORDER-016",
      "이미 완료된 주문의 상태를 변경할 수 없습니다."),
  NOT_CANCELABLE(
      HttpStatus.BAD_REQUEST, "ORDER-017",
      "해당 상품은 취소할 수 없는 상태입니다."),
  NOT_REFUNDABLE(
      HttpStatus.BAD_REQUEST, "ORDER-018",
      "해당 상품은 환불을 할 수 없는 상태입니다."),
  CANNOT_UPDATE_STATUS(
      HttpStatus.BAD_REQUEST, "ORDER-019",
      "해당 상품은 변경할 수 없는 상태입니다."),
  INVALID_STATUS_TRANSITION(
      HttpStatus.BAD_REQUEST, "ORDER-020",
      "적합하지 않은 상품 상태 변경입니다."),
  REFUND_PERIOD_EXPIRED(
      HttpStatus.BAD_REQUEST, "ORDER-021",
      "환불 규정 7일이내의 상품만 환불 요청이 가능합니다."),
  PRODUCT_NOT_FOUND(
      HttpStatus.BAD_REQUEST, "ORDER-022",
      "해당 상품을 주문하지 않았습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;
}