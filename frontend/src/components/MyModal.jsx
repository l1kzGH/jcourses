import React, {ReactElement, useEffect} from 'react'
import '../styles/modal.css'

interface ModalProps {
    visible: boolean,
    title: string,
    content: ReactElement | string,
    footer: ReactElement | string,
    onClose: () => void
}

const MyModal = ({
                   visible = false,
                   title = '',
                   content = '',
                   footer = '',
                   onClose,
               }: ModalProps) => {

    // нажатие клавиши ESC
    const onKeydown = ({ key }: KeyboardEvent) => {
        switch (key) {
            case 'Escape':
                onClose()
                break
        }
    }

    // c помощью useEffect цепляем обработчик к нажатию клавиш
    useEffect(() => {
        document.addEventListener('keydown', onKeydown)
        return () => document.removeEventListener('keydown', onKeydown)
    })

    if (!visible) return null

    return (
        <div className='modal' onClick={onClose}>
            <div className='modal-dialog' onClick={e => e.stopPropagation()}>
                <div className='modal-header'>
                    <h3 className='modal-title'>{title}</h3>
                </div>
                <div className='modal-body'>
                    <div className='modal-content'>{content}</div>
                </div>
            </div>
        </div>
    )
}

export default MyModal;