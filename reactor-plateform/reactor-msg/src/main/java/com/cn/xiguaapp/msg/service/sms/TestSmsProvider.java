package com.cn.xiguaapp.msg.service.sms;

import com.alibaba.fastjson.JSON;
import com.cn.xiguaapp.notify.api.constant.DefaultNotifyType;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.core.TemplateProperties;
import com.cn.xiguaapp.notify.api.param.NotifierProperties;
import com.cn.xiguaapp.notify.api.template.*;
import com.cn.xiguapp.common.core.core.config.ConfigMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Map;

@Slf4j
@Component
@Profile({"dev", "test"})
public class TestSmsProvider extends AbstractNotifier<PlainTextSmsTemplate> implements NotifierProvider, TemplateProvider, Provider {

    public TestSmsProvider(TemplateManager templateManager) {
        super(templateManager);
    }

    @Override
    @Nonnull
    public NotifyType getType() {
        return DefaultNotifyType.sms;
    }

    @Override
    @Nonnull
    public Provider getProvider() {
        return this;
    }

    @Override
    public Mono<? extends Template> createTemplate(TemplateProperties properties) {
        return Mono.fromSupplier(() -> JSON.parseObject(properties.getTemplate(), PlainTextSmsTemplate.class));
    }

    @Override
    public ConfigMetadata getTemplateConfigMetadata() {
        return PlainTextSmsTemplate.templateConfig;
    }

    @Override
    public ConfigMetadata getNotifierConfigMetadata() {
        return null;
    }

    @Nonnull
    @Override
    public Mono<Void> send(@Nonnull PlainTextSmsTemplate template, @Nonnull Map<String,Object> context) {
        return Mono.fromRunnable(() -> log.info("send sms {} message:{}", template.getSendTo(context), template.getTextSms(context)));
    }

    @Nonnull
    @Override
    public Mono<Void> close() {
        return Mono.empty();
    }

    @Nonnull
    @Override
    public Mono<TestSmsProvider> createNotifier(@Nonnull NotifierProperties properties) {
        return Mono.just(this);
    }

    @Override
    public Long getNotifierId() {
        return 0L;
    }

    @Override
    public String getId() {
        return "test";
    }

    @Override
    public String getName() {
        return "测试";
    }
}
